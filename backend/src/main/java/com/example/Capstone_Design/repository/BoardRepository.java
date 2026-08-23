package com.example.Capstone_Design.repository;

import com.example.Capstone_Design.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // 게시글 ID와 사용자 ID로 게시글 존재 여부 확인 (작성자 본인인지 확인용)
    boolean existsByIdAndUser_UserID(Long boardId, String userID);

    @Modifying
    @Transactional
    @Query("update BoardEntity b set b.likeCount = :likeCount where b.id = :id")
    void updateLikeCount(@Param("id") Long id, @Param("likeCount") int likeCount);

    //@Modifying이 붙은 JPA 쿼리는 데이터 수정용이기 때문에, 반환 타입은 오직 void 또는 int/Integer 만 허용됨.
}

// 📁 src/main/java/com/example/community/repository/CommentRepository.java
