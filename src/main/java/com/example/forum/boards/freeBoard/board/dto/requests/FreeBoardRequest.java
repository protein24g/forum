package com.example.forum.boards.freeBoard.board.dto.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class FreeBoardRequest {
    private String title;

    private String content;

    private List<String> originalImages; // 업데이트 기존 이미지

    private List<MultipartFile> images; // 새로운 이미지

}
