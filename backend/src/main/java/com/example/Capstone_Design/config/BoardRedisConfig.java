package com.example.Capstone_Design.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class BoardRedisConfig {
    @Bean
    public RedisTemplate<String, Object> boardRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // ✅ ObjectMapper 커스터마이징
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime, LocalDate 지원
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // → "2025-09-17T23:15:30" 형태로 저장 (타임스탬프 대신 ISO-8601 문자열)

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        // ✅ 직렬화 방식 설정
        template.setKeySerializer(new StringRedisSerializer());      // key → 문자열
        template.setValueSerializer(serializer);                     // value → JSON
        template.setHashKeySerializer(new StringRedisSerializer());  // hash key → 문자열
        template.setHashValueSerializer(serializer);                 // hash value → JSON

        return template;
    }

}
