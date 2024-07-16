package com.example.forum.boards.freeBoard.comment.entity;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.main.report.entity.Report;
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
public class FreeBoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "free_Board_id")
    private FreeBoard freeBoard;

    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "freeBoardComment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public FreeBoardComment(User user, FreeBoard freeBoard, String content, LocalDateTime createDate){
        this.user = user;
        this.freeBoard = freeBoard;
        this.content = content;
        this.createDate = createDate;
    }

    public void addReports(Report report){
        this.reports.add(report);
        report.setFreeBoardCommentReport(this);
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setBoard(FreeBoard freeBoard) { this.freeBoard = freeBoard; }

    public void setContent(String content) { this.content = content; };
}
