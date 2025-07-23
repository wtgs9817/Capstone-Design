package com.example.Capstone_Design.service;


import com.example.Capstone_Design.entity.CommentEntity;
import com.example.Capstone_Design.entity.CommentLikeEntity;
import com.example.Capstone_Design.entity.UserEntity;
import com.example.Capstone_Design.repository.CommentLikeRepository;
import com.example.Capstone_Design.repository.CommentRepository;
import com.example.Capstone_Design.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public int likeComment_2(Long id, UserDetails userDetails) {

        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 댓글은 존재하지 않습니다."));
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("해당 사용자는 존재하지 않습니다."));

        //이미 좋아요를 눌렀을 때
        if(commentLikeRepository.existsByCommentAndUser(comment, user)) {
           return -1;
        }

        CommentLikeEntity like = new CommentLikeEntity();
        like.setComment(comment);
        like.setUser(user);
        like.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul"))); // ✅ 한국 시간 설정
        commentLikeRepository.save(like);

        return 1;
    }

    public void unlikeComment_2(Long id, UserDetails userDetails) {

        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 댓글은 존재하지 않습니다."));
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("해당 사용자는 존재하지 않습니다."));

        commentLikeRepository.deleteByCommentAndUser(comment, user);
    }

    public boolean checkCommentLike_2(Long id, UserDetails userDetails) {

        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 댓글은 존재하지 않습니다."));
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("해당 사용자는 존재하지 않습니다."));

        boolean liked = commentLikeRepository.existsByCommentAndUser(comment, user);
        return liked;
    }
}
