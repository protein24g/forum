package com.example.forum.boards.freeboard.board.entity;

import com.example.forum.boards.freeboard.comment.entity.FreeBoardComment;
import com.example.forum.user.entity.User;
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
public class FreeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private User user;

    private int view;

    // CascadeType.REMOVE : 부모 Entity 삭제시 자식 Entity 들도 삭제
    // orphanRemoval = true : 부모 엔티티와의 관계가 끊어진 자식 엔티티들을 자동으로 삭제
    @OneToMany(mappedBy = "freeBoard", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FreeBoardComment> freeBoardComments = new ArrayList<>();

    @Builder
    public FreeBoard(String title, String content, LocalDateTime createDate, User user, int view){
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.user = user;
        this.view = view;
    }

    public void setUser(User user) { 
        this.user = user; 
    }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }

    public void addComment(FreeBoardComment freeBoardComment){
        this.freeBoardComments.add(freeBoardComment);
        freeBoardComment.setFreeBoard(this);
    }

    public int incView() {
        return ++this.view;
    }
}
