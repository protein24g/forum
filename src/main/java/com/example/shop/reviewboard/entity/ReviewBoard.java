package com.example.shop.reviewboard.entity;

import com.example.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReviewBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "review_score")
    private double reviewScore;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
