package com.example.forum.base.image.entity;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String fileName;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "free_board_id")
    private FreeBoard freeBoard;

    @Builder
    public Image(String originalName, String fileName, String filePath){
        this.originalName = originalName;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void setFreeBoard(FreeBoard freeBoard){
        this.freeBoard = freeBoard;
    }
}
