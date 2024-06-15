package com.example.forum.boards.questionBoard.comment.entity;

import com.example.forum.boards.questionBoard.board.entity.QuestionBoard;
import com.example.forum.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class QuestionBoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_Board_id")
    private QuestionBoard questionBoard;

    private String content;

    private LocalDateTime createDate;

    @Builder
    public QuestionBoardComment(User user, QuestionBoard questionBoard, String content, LocalDateTime createDate){
        this.user = user;
        this.questionBoard = questionBoard;
        this.content = content;
        this.createDate = createDate;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setBoard(QuestionBoard questionBoard) { this.questionBoard = questionBoard; }

    public void setContent(String content) { this.content = content; };
}
