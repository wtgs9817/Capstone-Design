package com.example.Capstone_Design.controller;

import com.example.Capstone_Design.dto.UserDTO;
import com.example.Capstone_Design.entity.EmailAuth;
import com.example.Capstone_Design.entity.UserEntity;
import com.example.Capstone_Design.repository.EmailAuthRepository;
import com.example.Capstone_Design.repository.UserRepository;
import com.example.Capstone_Design.service.EmailAuthService;
import com.example.Capstone_Design.service.FindPasswordService;
import com.example.Capstone_Design.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.*;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FindPasswordController {

    private final MailService mailService;
    private final FindPasswordService findPasswordService;
    private final EmailAuthService emailAuthService;
    /**
     * 비밀번호 재설정용 인증코드 이메일 전송
     */
    @PostMapping("/find-send-code")
    public ResponseEntity<Map<String, Object>> sendResetCode(@RequestBody UserDTO userDTO) {

        String email = userDTO.getUserID();  // 아이디 = 이메일
        Map<String, Object> response = findPasswordService.sendResetCode_2(email);

        if(!(Boolean) response.get("success")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 인증 코드 생성
        String code = UUID.randomUUID().toString().substring(0, 6);
        // 이메일 전송

        try {
            mailService.sendPasswordVerificationEmail(email, code);  // email = userID
            emailAuthService.emailAuthSave(email, code);

            response.put("success", true);
            response.put("message", "인증코드를 이메일로 보냈습니다.");
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }

    }

    /**
     * 이메일로 받은 인증코드 검증
     */
    @PostMapping("/password-verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String code = request.get("code");

        try{
            Map<String, Object> response = findPasswordService.verifyCode_2(email, code);
            if(!(Boolean) response.get("success")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * 인증 후 새로운 비밀번호 설정
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        try{
            Map<String, Object> response = findPasswordService.resetPassword_2(email, newPassword, confirmPassword);

            if(!(Boolean) response.get("success")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }

    }
}