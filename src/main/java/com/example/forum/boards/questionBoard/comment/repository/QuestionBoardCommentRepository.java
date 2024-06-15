package com.example.forum.boards.questionBoard.comment.repository;

import com.example.forum.boards.questionBoard.board.entity.QuestionBoard;
import com.example.forum.boards.questionBoard.comment.entity.QuestionBoardComment;
import com.example.forum.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionBoardCommentRepository extends JpaRepository<QuestionBoardComment, Long> {
    Page<QuestionBoardComment> findByQuestionBoardId(Long boardId, Pageable pageable);
    Page<QuestionBoardComment> findByUserId(Long userId, Pageable pageable);
    @Query("SELECT DISTINCT b.questionBoard FROM QuestionBoardComment b WHERE b.user = :user ORDER BY b.questionBoard.id DESC")
    Page<QuestionBoard> getquestionBoardByUserComments(User user, Pageable pageable);
}
