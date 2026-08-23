package com.example.Capstone_Design.SyncScheduler;



import com.example.Capstone_Design.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeCountSyncScheduler {

    private final BoardRepository boardRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    @Async  //비동기 실행 메인 스레드를 막지 않고 백그라운드 스레드에서 동작
    @Scheduled(fixedDelay= 3 * 60 * 1000)  // 애플리케이션이 실행 중일 때 5분마다 자동 호출
    public void likeCountsToDB() {
        Set<String> keys = redisTemplate.keys("boardlike:count:*");

        for(String key : keys) {
            try {
                Long boardId = getID(key);

                Object cached = redisTemplate.opsForValue().get(key);
                if(cached != null) {
                    int likeCount = Integer.parseInt(cached.toString());
                    boardRepository.updateLikeCount(boardId, likeCount);
                    log.info("게시글 ID {}의 좋아요 수 {} 저장 완료", boardId, likeCount);
                }
            } catch (Exception e) {
                log.error("게시글 {} 좋아요 동기화 중 오류 발생 : {} ", key, e.getMessage(), e);
            }
        }
    }

    public Long getID(String key) {
        return Long.parseLong(key.split(":")[2]);
    }
}
