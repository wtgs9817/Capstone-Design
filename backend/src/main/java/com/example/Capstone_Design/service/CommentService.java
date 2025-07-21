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
    public void createComment_2(Long boardId, CommentRequest commentRequest, UserDetails userDetails) {

        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        UserEntity user = userRepository.findByUserID(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        CommentEntity commentEntity = CommentEntity.builder().content(commentRequest.getContent()).board(board)
                .user(user).createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul"))).build();

        commentRepository.save(commentEntity);
    }

    @Transactional
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

    @Transactional
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
