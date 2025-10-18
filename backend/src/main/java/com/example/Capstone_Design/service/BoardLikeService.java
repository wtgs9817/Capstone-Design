package com.example.Capstone_Design.service;


import com.example.Capstone_Design.entity.BoardEntity;
import com.example.Capstone_Design.entity.BoardLikeEntity;
import com.example.Capstone_Design.entity.UserEntity;
import com.example.Capstone_Design.repository.BoardLikeRepository;
import com.example.Capstone_Design.repository.BoardRepository;
import com.example.Capstone_Design.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor

public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;



    public boolean checkBoardLike_2(Long id, UserDetails userDetails) {
        boolean liked = boardLikeRepository.existsByBoard_IdAndUser_UserID(id, userDetails.getUsername());
        return liked;
    }

    @Transactional
    public void likeBoard_2(Long id, UserDetails userDetails) {
        if (boardLikeRepository.existsByBoard_IdAndUser_UserID(id, userDetails.getUsername())) {
            throw new RuntimeException("이미 좋아요한 게시글입니다.");
        }

        BoardEntity board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        try {
            BoardLikeEntity like = new BoardLikeEntity(board, user);
            boardLikeRepository.save(like);
        }
        catch (DataIntegrityViolationException e) {
            throw new RuntimeException("이미 좋아요한 게시글입니다.");
        }

        String key ="boardlike:count:" + id;
        Object Cached = redisTemplate.opsForValue().get(key);

        String lockKey = "lock:boardlike:count" + id;

        if(Cached != null) {
            redisTemplate.opsForValue().increment(key);
        }
        else {
            RLock lock = redissonClient.getLock(lockKey);

            try {
                boolean locked = lock.tryLock(1,3, TimeUnit.SECONDS);
                if (locked) {
                    try {
                        Object cachedRetry = redisTemplate.opsForValue().get(key);
                        if(cachedRetry == null) {
                            BoardEntity likeCountBoard = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
                            int count = likeCountBoard.getLikeCount();

                            redisTemplate.opsForValue().set(key, count, Duration.ofMinutes(5));
                        }
                        redisTemplate.opsForValue().increment(key);
                    }
                    finally {
                        lock.unlock();

                    }
                }
            }
            catch (InterruptedException e) {
                //일단 보류 어떻게 처리할지 고민 중
            }


        }

       //board.setLikeCount(board.getLikeCount() + 1);
        //boardRepository.save(board);

    }

    // ✅ @Transactional 사용 시기 정리

    // 1. 단순히 save() 또는 delete() 하나만 호출하는 경우는
    //    Spring Data JPA 내부에서 트랜잭션을 자동 처리하므로 생략 가능하다.

    // 2. 그러나 Service 계층에서 조회, 조건검사, 가공 등 다른 로직과
    //    저장(save), 수정(update), 삭제(delete)가 함께 있다면
    //    반드시 @Transactional을 명시해줘야 한다.

    // 3. 이유는, 저장은 자동으로 커밋되더라도 앞이나 뒤 로직에서 예외가 발생하면
    //    전체를 롤백해야 데이터 정합성을 유지할 수 있기 때문이다.

    // 4. 트랜잭션은 "DB 작업 단위"가 아니라 "비즈니스 로직 단위"로 묶는 것이 바람직하다.
    //    따라서 트랜잭션 경계는 Repository가 아닌 Service 계층에서 관리해야 한다.

    // 5. 결론적으로, 실무에서는 대부분의 Service 계층 메서드에
    //    @Transactional을 붙이는 것이 안전하고 권장되는 설계 방식이다.
    @Transactional
    public void unlikeBoard_2(Long id, UserDetails userDetails) {

        BoardEntity board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        String userId = userDetails.getUsername();

        BoardLikeEntity like = boardLikeRepository.findByBoard_IdAndUser_UserID(id, userId)
                .orElseThrow(() -> new RuntimeException("좋아요 기록이 없습니다."));

        boardLikeRepository.delete(like);

        String key ="boardlike:count:" + id;
        Object Cached = redisTemplate.opsForValue().get(key);

        String lockKey = "lock:boardlike:count" + id;

        if(Cached != null) {
            redisTemplate.opsForValue().increment(key, -1);
        }
        else {
            RLock lock = redissonClient.getLock(lockKey);

            try {
                boolean locked = lock.tryLock(1,3, TimeUnit.SECONDS);
                if (locked) {
                    try {
                        Object cachedRetry = redisTemplate.opsForValue().get(key);
                        if(cachedRetry == null) {
                            BoardEntity likeCountBoard = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
                            int count = likeCountBoard.getLikeCount();

                            redisTemplate.opsForValue().set(key, count, Duration.ofMinutes(5));
                        }
                        redisTemplate.opsForValue().increment(key, -1);
                    }
                    finally {
                        lock.unlock();
                    }
                }
            }
            catch (InterruptedException e) {
                //일단 보류 어떻게 처리할지 고민 중
            }
        }

        //board.setLikeCount(Math.max(0, board.getLikeCount() - 1));
        //boardRepository.save(board);
    }

}
