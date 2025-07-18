package com.example.Capstone_Design.service;

import com.example.Capstone_Design.entity.EmailAuth;
import com.example.Capstone_Design.repository.EmailAuthRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Builder
public class EmailAuthService {
    private final EmailAuthRepository emailAuthRepository;

    @Transactional
    public void emailAuthSave(String email, String code) {

        EmailAuth emailAuth = EmailAuth.builder().email(email).code(code)
                .createdAt(LocalDateTime.now()).verified(false).build();

        emailAuthRepository.save(emailAuth);
    }

}
