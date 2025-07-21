package com.example.Capstone_Design.service;


import com.example.Capstone_Design.entity.BoardEntity;
import com.example.Capstone_Design.entity.BoardLikeEntity;
import com.example.Capstone_Design.entity.UserEntity;
import com.example.Capstone_Design.repository.BoardLikeRepository;
import com.example.Capstone_Design.repository.BoardRepository;
import com.example.Capstone_Design.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;



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

        BoardLikeEntity like = new BoardLikeEntity(board, user);
        boardLikeRepository.save(like);

        board.setLikeCount(board.getLikeCount() + 1);
        boardRepository.save(board);

    }

    @Transactional
    public void unlikeBoard_2(Long id, UserDetails userDetails) {

        BoardEntity board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        String userId = userDetails.getUsername();

        BoardLikeEntity like = boardLikeRepository.findByBoard_IdAndUser_UserID(id, userId)
                .orElseThrow(() -> new RuntimeException("좋아요 기록이 없습니다."));

        boardLikeRepository.delete(like);
        board.setLikeCount(Math.max(0, board.getLikeCount() - 1));
        boardRepository.save(board);

    }

}
