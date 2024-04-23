package com.example.shop.qnaboard.dto.requests;

import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaRequest {
    private Long id;

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    private boolean visibility;
}
