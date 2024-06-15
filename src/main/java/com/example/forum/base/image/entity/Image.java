package com.example.forum.base.image.entity;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.questionBoard.board.entity.QuestionBoard;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String fileName;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "free_board_id")
    private FreeBoard freeBoard;

    @ManyToOne
    @JoinColumn(name = "question_board_id")
    private QuestionBoard questionBoard;

    @Builder
    public Image(String originalName, String fileName, String filePath){
        this.originalName = originalName;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void setBoard(FreeBoard freeBoard){
        this.freeBoard = freeBoard;
    }

    public void setBoard(QuestionBoard questionBoard){
        this.questionBoard = questionBoard;
    }

    public enum Category {
        FREE("자유"),
        QUESTION("질문과_토론");

        private final String value;

        Category(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
