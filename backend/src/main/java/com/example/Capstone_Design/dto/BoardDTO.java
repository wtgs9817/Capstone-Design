package com.example.Capstone_Design.dto;

import com.example.Capstone_Design.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private String author;    // 작성자 이름 (userName)
    private String authorId;  // 작성자 ID (userID)
    private LocalDateTime createdAt;



    public static BoardDTO toBoardDTO(BoardEntity board, int commentCount) {

        BoardDTO dto = BoardDTO.builder().id(board.getId()).title(board.getTitle()).content(board.getContent())
                .likeCount(board.getLikeCount()).commentCount(commentCount).author(board.getUser().getUserName())
                .authorId(board.getUser().getUserID()).createdAt(board.getCreatedAt()).build();

        return dto;
    }


}