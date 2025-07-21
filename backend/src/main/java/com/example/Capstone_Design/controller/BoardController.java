package com.example.Capstone_Design.controller;

import com.example.Capstone_Design.dto.BoardDTO;
import com.example.Capstone_Design.dto.BoardRequest;

import com.example.Capstone_Design.service.BoardLikeService;
import com.example.Capstone_Design.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BoardController {


    private final BoardService boardService;
    private final BoardLikeService boardLikeService;



    // ✅ 게시글 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllBoards() {
        try{
            List<BoardDTO> result = boardService.getAllBoards_2();

            return ResponseEntity.ok(result);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    // ✅ 게시글 작성
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequest request,
                                         @AuthenticationPrincipal UserDetails userDetails) {

        try {
            boardService.createBoard_2(request, userDetails);
            return ResponseEntity.ok().body("게시글 작성 성공");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ✅ 게시글 수정 (403 문제 해결됨)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable Long id,
                                         @RequestBody BoardRequest request,
                                         @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try{
            // -1 : 작성자 불일치
            int flag = boardService.updateBoard_2(request, userDetails, id);

            if(flag == -1){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 작성자만 수정할 수 있습니다.");
            }
            else {
                return ResponseEntity.ok(Collections.singletonMap("message", "게시글 수정 완료"));
            }

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    // ✅ 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {

        try {
            int flag = boardService.deleteBoard_2(id, userDetails);

            if (flag == -1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("작성자만 삭제할 수 있습니다.");
            }

            return ResponseEntity.ok("게시글 삭제 완료");
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    // ✅ 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoard(@PathVariable Long id) {
        try{
            BoardDTO boardDTO = boardService.getBoard_2(id);
            return ResponseEntity.ok(boardDTO);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ 게시글 좋아요 상태 확인
    @GetMapping("/{id}/like")
    public ResponseEntity<?> checkBoardLike(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        try{
            boolean liked = boardLikeService.checkBoardLike_2(id, userDetails);
            return ResponseEntity.ok().body(java.util.Map.of("liked", liked));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ✅ 게시글 좋아요 추가
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeBoard(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        try {
            boardLikeService.likeBoard_2(id, userDetails);
            return ResponseEntity.ok("좋아요 성공");
        }
        //추후에 커스텀 예외클래스 생성 후 예외 구별해야 함.
        catch (Exception e) {
            if(e.getMessage().equals("이미 좋아요한 게시글입니다.")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ 좋아요 삭제
    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlikeBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {

        try {
            boardLikeService.unlikeBoard_2(id, userDetails);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok("좋아요 취소 완료");
    }
}
