package com.example.forum.boards.questionBoard.image.entity;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.questionBoard.board.entity.QuestionBoard;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class QuestionBoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String fileName;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "question_board_id")
    private QuestionBoard questionBoard;

    @Builder
    public QuestionBoardImage(String originalName, String fileName, String filePath){
        this.originalName = originalName;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void setBoard(QuestionBoard questionBoard){
        this.questionBoard = questionBoard;
    }
}
