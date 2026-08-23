package com.example.Capstone_Design.TestConfig;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestRedissonConfig {

    @Bean("TestRedissonClient")
    @Primary
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379")
                .setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(1)
                .setTimeout(3000);
        return Redisson.create(config);
    }
}
