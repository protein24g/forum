package com.example.forum.boards.freeBoard.comment.repository;

import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long> {
    Page<FreeBoardComment> findByFreeBoardId(Long boardId, Pageable pageable);
    Page<FreeBoardComment> findByUserId(Long userId, Pageable pageable);
    @Query("SELECT DISTINCT b.freeBoard FROM FreeBoardComment b WHERE b.user = :user ORDER BY b.freeBoard.id DESC")
    Page<FreeBoard> getfreeBoardByUserComments(User user, Pageable pageable);
}
