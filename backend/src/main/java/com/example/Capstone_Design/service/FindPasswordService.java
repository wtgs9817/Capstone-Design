package com.example.Capstone_Design.service;


import com.example.Capstone_Design.entity.EmailAuth;
import com.example.Capstone_Design.entity.UserEntity;
import com.example.Capstone_Design.repository.EmailAuthRepository;
import com.example.Capstone_Design.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindPasswordService {

    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder passwordEncoder;


    public Map<String, Object> sendResetCode_2(String email) {

        Map<String, Object> response = new HashMap<>();


        // DB에서 userID가 존재하는지 확인
        Optional<UserEntity> userOpt = userRepository.findById(email);  // userID가 PK인 경우

        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "가입되지 않은 이메일입니다.");

            return response;
        }
        response.put("success", true);

        return response;
    }

    public Map<String, Object> verifyCode_2(String email, String code) {
        Map<String, Object> response = new HashMap<>();

        List<EmailAuth> authList = emailAuthRepository.findAllByEmailOrderByCreatedAtDesc(email);
        if (authList.isEmpty()) {
            response.put("success", false);
            response.put("message", "이메일 정보가 없습니다.");
            return response;
        }

        EmailAuth auth = authList.get(0);
        if (auth.isVerified()) {
            response.put("success", false);
            response.put("message", "이미 인증이 완료된 이메일입니다.");
            return response;
        }

        if (!auth.getCode().equals(code)) {
            response.put("success", false);
            response.put("message", "인증코드가 일치하지 않습니다.");
            return response;
        }

        auth.setVerified(true);
        emailAuthRepository.save(auth);

        response.put("success", true);
        response.put("message", "인증이 완료되었습니다.");

        return response;
    }

    public Map<String, Object> resetPassword_2(String email, String confirmPassword, String newPassword) {
        Map<String, Object> response = new HashMap<>();

        // 비밀번호 일치 확인
        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            response.put("success", false);
            response.put("message", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return response;
        }

        // 인증 여부 확인
        List<EmailAuth> authList = emailAuthRepository.findAllByEmailOrderByCreatedAtDesc(email);
        if (authList.isEmpty() || !authList.get(0).isVerified()) {
            response.put("success", false);
            response.put("message", "이메일 인증이 완료되지 않았습니다.");
            return response;
        }

        // 비밀번호 변경
        Optional<UserEntity> userOpt = userRepository.findById(email);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "가입된 사용자 정보를 찾을 수 없습니다.");
            return response;
        }

        UserEntity user = userOpt.get();
        String encryptedPwd = passwordEncoder.encode(newPassword);
        user.setPwd(encryptedPwd);
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
        return response;

    }



}
