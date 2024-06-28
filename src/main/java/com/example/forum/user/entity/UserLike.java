package com.example.forum.user.entity;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "free_board_id")
    private FreeBoard freeBoard;

    public void setUser(User user){
        this.user = user;
    }

    public void setFreeBoard(FreeBoard freeBoard){
        this.freeBoard = freeBoard;
    }
}
