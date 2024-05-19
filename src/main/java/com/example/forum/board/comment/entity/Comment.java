package com.example.forum.board.comment.entity;

import com.example.forum.board.freeboard.entity.Board;
import com.example.forum.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user = null;

    @ManyToOne
    private Board board = null;

    private String content;

    private LocalDateTime createDate;

    @Builder
    public Comment(User user, Board board, String content, LocalDateTime createDate){
        this.user = user;
        this.board = board;
        this.content = content;
        this.createDate = createDate;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setBoard(Board board) { this.board = board; }

    public void setContent(String content) { this.content = content; };
}
