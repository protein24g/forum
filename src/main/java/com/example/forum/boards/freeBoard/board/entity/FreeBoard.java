package com.example.forum.boards.freeBoard.board.entity;

import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
import com.example.forum.boards.freeBoard.image.entity.FreeBoardImage;
import com.example.forum.main.report.entity.Report;
import com.example.forum.user.entity.User;
import com.example.forum.user.entity.UserLike;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class FreeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private LocalDateTime createDate;

    private int view;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // CascadeType.REMOVE : 부모 Entity 삭제시 자식 Entity 들도 삭제
    // orphanRemoval = true : 부모 엔티티와의 관계가 끊어진 자식 엔티티들을 자동으로 삭제
    @OneToMany(mappedBy = "freeBoard", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "freeBoard", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<UserLike> userLikes = new HashSet<>();

    @OneToMany(mappedBy = "freeBoard", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FreeBoardImage> images = new ArrayList<>();

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

    public void addReports(Report report){
        this.reports.add(report);
        report.setFreeBoardReport(this);
    }

    public void setBoard(User user){
        this.user = user;
    }

    public void addUserLikes(UserLike userLike){
        this.userLikes.add(userLike);
        userLike.setFreeBoard(this);
    }

    public void addImage(FreeBoardImage freeBoardImage){
        this.images.add(freeBoardImage);
        freeBoardImage.setBoard(this);
    }

    public void addComment(FreeBoardComment freeBoardComment){
        this.freeBoardComments.add(freeBoardComment);
        freeBoardComment.setBoard(this);
    }

    public int incView() {
        return ++this.view;
    }
}
