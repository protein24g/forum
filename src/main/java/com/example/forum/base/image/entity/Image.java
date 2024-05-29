package com.example.forum.base.image.entity;

import com.example.forum.boards.freeboard.board.entity.FreeBoard;
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

    private String fileName;

    private String filePath;

    @ManyToOne
    private FreeBoard freeBoard = null;

    @Builder
    public Image(String fileName, String filePath){
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void setFreeBoard(FreeBoard freeBoard){
        this.freeBoard = freeBoard;
    }
}
