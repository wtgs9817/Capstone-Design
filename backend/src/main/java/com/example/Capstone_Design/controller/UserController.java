package com.example.Capstone_Design.controller;

import com.example.Capstone_Design.dto.MyPageResponse;
import com.example.Capstone_Design.dto.UserDTO;

import com.example.Capstone_Design.service.EmailAuthService;
import com.example.Capstone_Design.service.MailService;
import com.example.Capstone_Design.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final EmailAuthService emailAuthService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {

        try{
            Map<String, Object> response = userService.register_2(userDTO);

            if(!(boolean) response.get("success")) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/send-code")
    public ResponseEntity<Map<String, Object>> sendEmailCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();
        try {
            response = userService.checkEmail(email);

            if(!(boolean) response.get("success")) {
                return ResponseEntity.badRequest().body(response);
            }

            String code = UUID.randomUUID().toString().substring(0, 6);

            mailService.sendVerificationEmail(email, code);
            emailAuthService.emailAuthSave(email, code);

            response.put("success", true);
            response.put("message", "인증 메일이 발송되었습니다.");

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, Object>> verifyEmailCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        Map<String, Object> response = new HashMap<>();

        try {
            response = userService.verifyEmailCode_2(email, code);

            if(!(boolean) response.get("success")) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/mypage/user")
    public ResponseEntity<?> getMyPageUser(@AuthenticationPrincipal UserDetails userDetails) {

        try{
            String userID = userDetails.getUsername();

            MyPageResponse myPageResponse = userService.getMyPageUser_2(userID);
            return ResponseEntity.ok(myPageResponse);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }



    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {

        try{
            UserDTO userDTO = userService.getCurrentUser_2(userDetails);
            return ResponseEntity.ok(userDTO);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


