package com.example.Capstone_Design.controller;



import com.example.Capstone_Design.dto.*;
import com.example.Capstone_Design.service.GraduationCheckService;
import com.example.Capstone_Design.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GraduationCheckController {

    private final GraduationCheckService graduationCheckService;
    private final UserService userService;



    //주전공,복수전공 총학점 리턴
    @PostMapping("/total-score")
    public ResponseEntity<?> totalSubjectScore(@AuthenticationPrincipal UserDetails userDetails) {

        try{
            String userID = userDetails.getUsername();
            UserDTO user = userService.getUser(userID);

            String studentNumber = user.getStudentNumber();

            int totalScore = graduationCheckService.totalSubjectScore(studentNumber);
            Map<String, Integer> map = graduationCheckService.getSubjectScore(totalScore);

            return ResponseEntity.ok(map);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    //학생이 수강하고 있는 과목중에서 과에 맞는 필수전공 리스트 (주전공)
    @PostMapping("/graduation-check")
    public ResponseEntity<?> graduationCheck(@AuthenticationPrincipal UserDetails userDetails) {

        try{
            String userID = userDetails.getUsername();
            UserDTO user = userService.getUser(userID);

            String majorCode = graduationCheckService.getMajorCode(user.getMajor());

            List<GraduationCheckDTO> allList = graduationCheckService.graduationSubject(majorCode);
            List<GraduationCheckDTO> checkList = graduationCheckService.graduationCheck(user.getStudentNumber(), majorCode);

            List<GraduationCheckResponse> list = graduationCheckService.getSubjectCheckList(allList, checkList, user.getStudentNumber(), majorCode);

            return ResponseEntity.ok(list);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //학생이 수강하고 있는 과목중에서 과에 맞는 필수전공 리스트 (복수 전공) 임시코드
    @PostMapping("/graduation-check02")
    public ResponseEntity<?> graduationCheck02(@AuthenticationPrincipal UserDetails userDetails) {

        try{
            String userID = userDetails.getUsername();
            UserDTO user = userService.getUser(userID);

            String majorCode = graduationCheckService.getMajorCode(user.getScdMajor());

            List<GraduationCheckDTO> allList = graduationCheckService.graduationSubject(majorCode);
            List<GraduationCheckDTO> checkList = graduationCheckService.graduationCheck(user.getStudentNumber(), majorCode);

            List<GraduationCheckResponse> list = graduationCheckService.getSubjectCheckList(allList, checkList, user.getStudentNumber(), majorCode);

            return ResponseEntity.ok(list);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //현재 수강하고 있는 과목
    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects(@AuthenticationPrincipal UserDetails userDetails) {

        try{
            String userID = userDetails.getUsername();
            UserDTO user = userService.getUser(userID);

            String studentNumber = user.getStudentNumber();

            List<GraduationCheckDTO> list = graduationCheckService.getSubjects(studentNumber);
            List<String> subjects = graduationCheckService.getGraduationSubjectList(list);

            return ResponseEntity.ok(subjects);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //선택한 과목 저장
    @PutMapping("/subjects")
    public ResponseEntity<?> subjectSave(@AuthenticationPrincipal UserDetails userDetails, @RequestBody SubjectListRequest request) {

        try{
            String userID = userDetails.getUsername();
            UserDTO user = userService.getUser(userID);

            String studentNumber = user.getStudentNumber();
            List<String> subjectNames = request.getSubjects();

            boolean saveFlag = graduationCheckService.studentSubjectSave(studentNumber, subjectNames, user);

            if(!saveFlag) {
                return ResponseEntity.badRequest().body("DB에 존재하는 과목명과 일치하지 않습니다.");
            }
            else {
                return ResponseEntity.ok().body("과목 저장 성공");
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    //졸업 요건 리스트 충족 불충족 여부
    @PostMapping("/graduation-check-result")
    public ResponseEntity<?> graduationCheckResult(@AuthenticationPrincipal UserDetails userDetails) {

        try{
            String userID = userDetails.getUsername();
            UserDTO user = userService.getUser(userID);

            String studentNumber = user.getStudentNumber();
            String majorCode = graduationCheckService.getMajorCode(user.getMajor());
            String scdMajorCode = graduationCheckService.getMajorCode(user.getScdMajor());

            Map<String, String> list = graduationCheckService.getGraduationCheckResults(studentNumber, majorCode, scdMajorCode);

            return ResponseEntity.ok(list);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


}