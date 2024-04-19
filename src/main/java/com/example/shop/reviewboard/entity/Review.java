package com.example.shop.reviewboard.entity;

import com.example.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "review_score")
    private double reviewScore;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Review(String title, String content, double reviewScore, User user, LocalDateTime createDate){
        this.title = title;
        this.content = content;
        this.reviewScore = reviewScore;
        this.user = user;
        this.createDate = createDate;
    }
}
