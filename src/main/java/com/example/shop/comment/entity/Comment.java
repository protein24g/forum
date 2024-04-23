package com.example.shop.comment.entity;

import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user = null;

    @ManyToOne
    private QuestionAndAnswer questionAndAnswer = null;

    private String content;

    private LocalDateTime createDate;

    @Builder
    public Comment(User user, QuestionAndAnswer questionAndAnswer, String content, LocalDateTime createDate){
        this.user = user;
        this.questionAndAnswer = questionAndAnswer;
        this.content = content;
        this.createDate = createDate;
    }

    public void setQuestionAndAnswer(QuestionAndAnswer questionAndAnswer){
        this.questionAndAnswer = questionAndAnswer;
    }

    public void setUser(User user){
        this.user = user;
    }
}
