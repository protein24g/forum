package com.example.forum.boards.freeboard.comment.entity;

import com.example.forum.boards.freeboard.board.entity.FreeBoard;
import com.example.forum.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class FreeBoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user = null;

    @ManyToOne
    private FreeBoard freeBoard = null;

    private String content;

    private LocalDateTime createDate;

    @Builder
    public FreeBoardComment(User user, FreeBoard freeBoard, String content, LocalDateTime createDate){
        this.user = user;
        this.freeBoard = freeBoard;
        this.content = content;
        this.createDate = createDate;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setFreeBoard(FreeBoard freeBoard) { this.freeBoard = freeBoard; }

    public void setContent(String content) { this.content = content; };
}
