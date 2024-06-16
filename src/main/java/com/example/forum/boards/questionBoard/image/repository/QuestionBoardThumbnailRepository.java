package com.example.forum.boards.questionBoard.image.repository;


import com.example.forum.boards.questionBoard.image.entity.QuestionBoardThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBoardThumbnailRepository extends JpaRepository<QuestionBoardThumbnail, Long> {
    List<QuestionBoardThumbnail> findByQuestionBoardId(Long boardId);
}
