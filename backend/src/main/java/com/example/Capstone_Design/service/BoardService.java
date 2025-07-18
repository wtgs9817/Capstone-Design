package com.example.Capstone_Design.service;


import com.example.Capstone_Design.dto.BoardDTO;
import com.example.Capstone_Design.dto.BoardRequest;
import com.example.Capstone_Design.entity.BoardEntity;
import com.example.Capstone_Design.entity.UserEntity;
import com.example.Capstone_Design.repository.BoardLikeRepository;
import com.example.Capstone_Design.repository.BoardRepository;
import com.example.Capstone_Design.repository.CommentRepository;
import com.example.Capstone_Design.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardLikeRepository boardLikeRepository;

    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    // 게시글 목록 조회
    public List<BoardDTO> getAllBoards_2() {

        List<BoardDTO> result = boardRepository.findAll().stream().map(board -> {
            int commentCount = commentRepository.countByBoardId(board.getId());
            return new BoardDTO(
                    board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    board.getLikeCount(),
                    commentCount,
                    board.getUser().getUserName(),
                    board.getUser().getUserID(),
                    board.getCreatedAt()
            );
        }).toList();

        return result;
    }

    @Transactional
    public void createBoard_2(BoardRequest request, UserDetails userDetails) {

        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow();
        LocalDateTime now = LocalDateTime.now(SEOUL_ZONE);

        BoardEntity boardEntity = BoardEntity.builder().title(request.getTitle())
                .content(request.getContent()).user(user).likeCount(0).createdAt(now).updatedAt(now).build();

        boardRepository.save(boardEntity);
    }

    @Transactional
    public int updateBoard_2(BoardRequest request, UserDetails userDetails, Long id) {

        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        UserEntity user = userRepository.findByUserID(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));

        if(!boardEntity.getUser().getUserID().equals(user.getUserID())) {
            return -1;
        }

        boardEntity.setTitle(request.getTitle());
        boardEntity.setContent(request.getContent());
        boardEntity.setUpdatedAt(LocalDateTime.now(SEOUL_ZONE));
        boardRepository.save(boardEntity);

        return 1;

    }

    @Transactional
    public int deleteBoard_2(Long id, UserDetails userDetails) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        UserEntity user = userRepository.findByUserID(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));

        if (!boardEntity.getUser().getUserID().equals(user.getUserID())) {
            return -1;
        }

        else {
            boardRepository.delete(boardEntity);
            return 1;
        }
    }

    public BoardDTO getBoard_2(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        int commentCount = commentRepository.countByBoardId(boardEntity.getId());
        BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity, commentCount);

        return boardDTO;
    }


















}
