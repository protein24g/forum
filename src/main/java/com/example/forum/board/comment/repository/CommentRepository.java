package com.example.forum.board.comment.repository;

import com.example.forum.board.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);

    Page<Comment> findByUserId(Long userId, Pageable pageable);
}