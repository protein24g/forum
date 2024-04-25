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

    // CascadeType.REMOVE : 부모 Entity 삭제시 자식 Entity 들도 삭제
    // orphanRemoval = true : 부모 엔티티와의 관계가 끊어진 자식 엔티티들을 자동으로 삭제
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
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
