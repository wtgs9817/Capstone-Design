package com.example.Capstone_Design.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://redis:6379")
                .setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(1)
                .setTimeout(3000);
        return Redisson.create(config);
    }
}

/*
설정 항목	                    의미	                    추천 설정
setAddress	                    Redis 서버 주소	        redis://IP:PORT
setConnectionMinimumIdleSize	최소 유지 커넥션 수	    1~2
setConnectionPoolSize	        최대 커넥션 풀 수	        트래픽에 따라 조절 (예: 10~50)
setTimeout	                    Redis 명령 응답 대기 시간	3000~5000ms
setRetryAttempts	            실패 시 재시도 횟수	    3
setRetryInterval	            재시도 간격	            1500ms

 */