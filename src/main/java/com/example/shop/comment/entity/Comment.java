package com.example.shop.comment.entity;

import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private QuestionAndAnswer questionAndAnswer;

    private String content;

    private LocalDateTime createDate;
}
