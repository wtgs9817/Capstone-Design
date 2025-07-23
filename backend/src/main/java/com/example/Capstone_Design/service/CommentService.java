package com.example.Capstone_Design.service;

import com.example.Capstone_Design.dto.CommentDTO;
import com.example.Capstone_Design.dto.CommentRequest;
import com.example.Capstone_Design.entity.BoardEntity;
import com.example.Capstone_Design.entity.CommentEntity;
import com.example.Capstone_Design.entity.UserEntity;
import com.example.Capstone_Design.repository.BoardRepository;
import com.example.Capstone_Design.repository.CommentRepository;
import com.example.Capstone_Design.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<CommentDTO> getComments_2(Long boardId) {
        List<CommentDTO> result = commentRepository.findByBoardId(boardId).stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getUserName(),
                        comment.getUser().getUserID(),
                        comment.getCreatedAt()
                ))
                .toList();

        return result;
    }



    public void createComment_2(Long boardId, CommentRequest commentRequest, UserDetails userDetails) {

        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        CommentEntity commentEntity = CommentEntity.builder().content(commentRequest.getContent()).board(board)
                .user(user).createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul"))).build();

        commentRepository.save(commentEntity);
    }


    public int updateComment_2(Long id, CommentRequest commentRequest, UserDetails userDetails) {

        CommentEntity comment = commentRepository.findById(id).orElseThrow( () -> new RuntimeException("해당 댓글은 존재하지 않습니다."));
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("해당 사용자는 존재하지 않습니다."));

        if (!comment.getUser().getUserID().equals(user.getUserID())) {
            return -1;
        }
        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);

        return 1;
    }


    public int deleteComment_2(Long id, UserDetails userDetails) {

        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 댓글은 존재하지 않습니다."));
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("해당 사용자는 존재하지 않습니다."));

        if (!comment.getUser().getUserID().equals(user.getUserID())) {
            return -1;
        }

        commentRepository.delete(comment);
        return 1;
    }

}
