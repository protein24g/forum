package com.example.forum.boards.questionBoard.image.repository;

import com.example.forum.boards.questionBoard.image.entity.QuestionBoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBoardImageRepository extends JpaRepository<QuestionBoardImage, Long> {
    List<QuestionBoardImage> findByQuestionBoardId(Long boardId);
}
