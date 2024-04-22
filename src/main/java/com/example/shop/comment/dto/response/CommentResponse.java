package com.example.shop.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponse {
    private String nickname;
    private String content;
    private LocalDateTime createDate;

    @Builder
    public CommentResponse(String nickname, String content, LocalDateTime createDate){
        this.nickname = nickname;
        this.content = content;
        this.createDate = createDate;
    }
}
