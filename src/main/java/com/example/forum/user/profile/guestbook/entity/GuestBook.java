package com.example.forum.user.profile.guestbook.entity;

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
public class GuestBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private LocalDateTime createDate;

    private Long targetId;

    @OneToMany(mappedBy = "guestBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public GuestBook(String content, LocalDateTime createDate, Long targetId){
        this.content = content;
        this.createDate = createDate;
        this.targetId = targetId;
    }

    public void addReports(Report report){
        this.reports.add(report);
        report.setGuestBookReport(this);
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setContent(String content){
        this.content = content;
    }
}
