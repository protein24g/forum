package com.example.shop.reviewboard.entity;

import com.example.shop.comment.entity.Comment;
import com.example.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Review(String title, String content, LocalDateTime createDate, User user){
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setReview(this);
    }
}
