package com.example.forum.board.comment.repository;

import com.example.forum.board.comment.entity.Comment;
import com.example.forum.board.freeboard.entity.Board;
import com.example.forum.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    @Query("SELECT DISTINCT b.board FROM Comment b WHERE b.user = :user ORDER BY b.board.id DESC")
    Page<Board> getBoardsByUserComments(User user, Pageable pageable);
}
