package com.example.Capstone_Design.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    // 이메일 인증 메일 전송
    public void sendVerificationEmail(String toEmail, String code) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("회원가입 이메일 인증");
        message.setText("안녕하세요!\n\n인증 코드는 다음과 같습니다:\n\n👉 " + code + "\n\n사이트에 돌아가서 인증 코드를 입력해주세요.");
        message.setFrom("20203305@hallym.ac.kr");

        mailSender.send(message);
    }

    // 비밀번호 변경  이메일 인증 메일 전송
    public void sendPasswordVerificationEmail(String toEmail, String code) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("비밀번호 변경 이메일 인증");
        message.setText("안녕하세요!\n\n인증 코드는 다음과 같습니다:\n\n👉 " + code + "\n\n사이트에 돌아가서 인증 코드를 입력해주세요.");
        message.setFrom("20203305@hallym.ac.kr");

        mailSender.send(message);
    }
}