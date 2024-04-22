package com.example.shop.qnaboard.entity;

import com.example.shop.comment.entity.Comment;
import com.example.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class QuestionAndAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "questionAndAnswer", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Builder
    public QuestionAndAnswer(String title, String content, User user, LocalDateTime createDate){
        this.title = title;
        this.content = content;
        this.user = user;
        this.createDate = createDate;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
