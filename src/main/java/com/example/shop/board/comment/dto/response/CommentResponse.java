package com.example.shop.board.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponse {
    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createDate;

    @Builder
    public CommentResponse(Long id, String nickname, String content, LocalDateTime createDate){
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createDate = createDate;
    }
}
