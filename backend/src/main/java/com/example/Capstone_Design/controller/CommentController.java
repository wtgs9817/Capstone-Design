package com.example.Capstone_Design.controller;

import com.example.Capstone_Design.dto.CommentDTO;
import com.example.Capstone_Design.dto.CommentRequest;

import com.example.Capstone_Design.service.CommentLikeService;
import com.example.Capstone_Design.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    // ✅ 댓글 목록
    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getComments(@PathVariable Long boardId) {
        try {
            List<CommentDTO> result = commentService.getComments_2(boardId);
            return ResponseEntity.ok(result);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ 댓글 작성
    @PostMapping("/board/{boardId}")
    public ResponseEntity<?> createComment(@PathVariable Long boardId,
                                           @RequestBody CommentRequest commentRequest,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        try {
            commentService.createComment_2(boardId, commentRequest, userDetails);
            return ResponseEntity.ok("댓글 작성 완료");

        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ 댓글 수정
    //추후에 커스텀 예외 클래스 생성 후 예외 처리 예정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id,
                                           @RequestBody CommentRequest commentRequest,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        try{
            int flag = commentService.updateComment_2(id, commentRequest, userDetails);

            //작성자 불일치 -> -1
            if(flag == -1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("작성자만 수정할 수 있습니다.");
            }
            return ResponseEntity.ok("댓글 수정 완료");
        }

        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    // ✅ 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        try{
            int flag = commentService.deleteComment_2(id, userDetails);

            //작성자 불일치 -> -1
            if(flag == -1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("작성자만 삭제할 수 있습니다.");
            }
            return ResponseEntity.ok("삭제 완료");
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /* 댓글 좋아요 기능 프론트엔드 쪽에서 미구현

    // ✅ 댓글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeComment(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            int flag = commentLikeService.likeComment_2(id, userDetails);
            //이미 좋아요를 눌렀을 때
            if(flag == -1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 좋아요한 댓글입니다.");
            }
            return ResponseEntity.ok("좋아요 성공");
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ 댓글 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlikeComment(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try{
            commentLikeService.unlikeComment_2(id, userDetails);
            return ResponseEntity.ok("좋아요 취소");
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ 댓글 좋아요 상태 확인
    @GetMapping("/{id}/like")
    public ResponseEntity<?> checkCommentLike(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        boolean liked = commentLikeService.checkCommentLike_2(id, userDetails);
        return ResponseEntity.ok(Map.of("liked", liked));
    }

     */
}
