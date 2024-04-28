package com.example.shop.board.comment.entity;

import com.example.shop.board.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.board.reviewboard.entity.Review;
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

    @ManyToOne
    private Review review = null;

    private String content;

    private LocalDateTime createDate;

    @Builder
    public Comment(User user, QuestionAndAnswer questionAndAnswer, Review review, String content, LocalDateTime createDate){
        this.user = user;
        this.questionAndAnswer = questionAndAnswer;
        this.review = review;
        this.content = content;
        this.createDate = createDate;
    }

    public void setQuestionAndAnswer(QuestionAndAnswer questionAndAnswer){
        this.questionAndAnswer = questionAndAnswer;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setReview(Review review) { this.review = review; }

    public void setContent(String content) { this.content = content; };
}
