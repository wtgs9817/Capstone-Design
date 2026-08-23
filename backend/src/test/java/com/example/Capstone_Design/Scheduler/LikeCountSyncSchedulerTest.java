package com.example.Capstone_Design.Scheduler;


import com.example.Capstone_Design.SyncScheduler.LikeCountSyncScheduler;
import com.example.Capstone_Design.TestConfig.TestRedisConfig;
import com.example.Capstone_Design.TestConfig.TestRedissonConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@SpringBootTest
@Import({TestRedisConfig.class, TestRedissonConfig.class})

public class LikeCountSyncSchedulerTest {

    @Autowired
    LikeCountSyncScheduler scheduler;



    @Test
    void testLikeCountsToDB() {
        scheduler.likeCountsToDB();
    }

}
