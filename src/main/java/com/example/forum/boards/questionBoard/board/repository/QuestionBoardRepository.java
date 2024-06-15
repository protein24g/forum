package com.example.forum.boards.questionBoard.board.repository;

import com.example.forum.boards.questionBoard.board.entity.QuestionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
    Page<QuestionBoard> findByTitleContaining(String keyword, Pageable pageable);
    Page<QuestionBoard> findByContentContaining(String keyword, Pageable pageable);
    Page<QuestionBoard> findByUserId(Long userId, Pageable pageable);
}
