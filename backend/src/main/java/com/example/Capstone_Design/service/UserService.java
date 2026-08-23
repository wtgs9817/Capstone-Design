package com.example.Capstone_Design.service;

import com.example.Capstone_Design.Exception.BadRequestException;
import com.example.Capstone_Design.Exception.UserNotFoundException;
import com.example.Capstone_Design.dto.MyPageResponse;
import com.example.Capstone_Design.dto.UserDTO;
import com.example.Capstone_Design.entity.EmailAuth;
import com.example.Capstone_Design.entity.UserEntity;
import com.example.Capstone_Design.repository.EmailAuthRepository;
import com.example.Capstone_Design.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; // jpa, MySQL dependency 추가
    private final PasswordEncoder passwordEncoder; // ✅ 비밀번호 암호화용
    private final EmailAuthRepository emailAuthRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserEntity login(String userID, String rawPassword) {
        UserEntity user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPwd())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user; // 현재는 세션 X, 나중에 JWT 토큰 방식 쓸 예정
    }

    public UserDTO getUser(String userID) {
        if(userID == null || userID.isBlank()) {
            throw new BadRequestException("userID이 정상적으로 넘어오지 않음.");
        }
        String key = "user:" + userID;

        Object cached = redisTemplate.opsForValue().get(key);
        if(cached != null) {
            return (UserDTO) cached;
        }

        UserEntity user = userRepository.findByUserID(userID).orElseThrow(() -> new UserNotFoundException("입력하신 회원이 존재하지 않습니다."));
        UserDTO userDTO = UserDTO.toUserDTO(user);

        redisTemplate.opsForValue().set(key, userDTO, Duration.ofMinutes(30));
        return userDTO;
    }

    public Map<String, Object> register_2(UserDTO userDTO) {

        Map<String, Object> response = new HashMap<>();
        String email = userDTO.getUserID(); // 아이디 = 이메일

        if (userDTO.getPwd() == null || !userDTO.getPwd().equals(userDTO.getPasswordCheck())) {
            response.put("success", false);
            response.put("message", "비밀번호가 일치하지 않습니다.");
            return response;
        }

        if (userRepository.existsByUserID(email)) {
            response.put("success", false);
            response.put("message", "이미 가입된 이메일입니다.");
            return response;
        }


        List<EmailAuth> authList = emailAuthRepository.findAllByEmailOrderByCreatedAtDesc(userDTO.getUserID());
        if (authList.isEmpty() || !authList.get(0).isVerified()) {
            response.put("success", false);
            response.put("message", "이메일 인증이 완료되지 않았습니다.");
            return response;
        }

        // ✅ 비밀번호 암호화
        String encodedPwd = passwordEncoder.encode(userDTO.getPwd());
        userDTO.setPwd(encodedPwd);

        // request -> DTO -> Entity -> Repository에서 save
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
        // ✅ 기본 권한 USER (role_id = 1)
        userEntity.setRoleId(1);
        userRepository.save(userEntity);
        //Repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)

        response.put("success", true);
        response.put("message", "회원가입 성공");

        return response;
    }

    public Map<String, Object> checkEmail(String email) {
        Map<String, Object> response = new HashMap<>();

        if(userRepository.existsByUserID(email)) {
            response.put("success", false);
            response.put("message", "이미 가입된 이메일입니다.");
            return response;
        }

        response.put("success", true);
        return response;
    }

    public Map<String, Object> verifyEmailCode_2(String email, String code) {

        Map<String, Object> response = new HashMap<>();

        List<EmailAuth> authList = emailAuthRepository.findAllByEmailOrderByCreatedAtDesc(email);
        if (authList.isEmpty()) {
            response.put("verified", false);
            response.put("message", "이메일 정보가 없습니다.");
            return response;
        }

        EmailAuth auth = authList.get(0);
        if (auth.isVerified()) {
            response.put("verified", false);
            response.put("message", "이미 인증된 사용자입니다.");
            return response;
        }

        if (!auth.getCode().equals(code)) {
            response.put("verified", false);
            response.put("message", "인증 코드가 일치하지 않습니다.");
            return response;
        }

        auth.setVerified(true);
        emailAuthRepository.save(auth);

        response.put("success", true);
        response.put("verified", true);
        response.put("message", "이메일 인증이 완료되었습니다!");

        return response;
    }




    public MyPageResponse getMyPageUser_2(String userID) {
        if(userID == null || userID.isBlank()) {
            throw new BadRequestException("userID이 정상적으로 넘어오지 않음.");
        }

        UserEntity user = userRepository.findByUserID(userID).orElseThrow(() -> new UserNotFoundException("입력하신 회원이 존재하지 않습니다."));
        return MyPageResponse.toMyPageResponse(user);
    }

    public UserDTO getCurrentUser_2(UserDetails userDetails) {
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("해당 사용자는 존재하지 않습니다."));
        UserDTO userDTO = UserDTO.toUserDTO(user);

        return userDTO;
    }






}