package com.example.forum.boards.questionBoard.image.entity;

import com.example.forum.boards.questionBoard.board.entity.QuestionBoard;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class QuestionBoardThumbnail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String fileName;

    private String filePath;

    private LocalDateTime createDate;

    @OneToOne
    @JoinColumn(name = "question_board_id")
    private QuestionBoard questionBoard;

    @Builder
    public QuestionBoardThumbnail(String originalName, String fileName, String filePath, LocalDateTime createDate){
        this.originalName = originalName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.createDate = createDate;
    }

    public void setBoard(QuestionBoard questionBoard){
        this.questionBoard = questionBoard;
    }
}
