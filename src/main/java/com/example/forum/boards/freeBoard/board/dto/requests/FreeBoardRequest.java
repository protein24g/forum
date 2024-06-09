package com.example.forum.boards.freeBoard.board.dto.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class FreeBoardRequest {
    private String title;

    private String content;

    private List<MultipartFile> images;
}
