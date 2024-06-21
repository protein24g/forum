package com.example.forum.boards.freeBoard.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@Getter
@Setter
@ToString
public class FreeBoardRequest {
    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "내용을 입력하세요")
    private String content;

    private List<String> originalImages; // 업데이트 기존 이미지

    private List<MultipartFile> images; // 새로운 이미지

}
