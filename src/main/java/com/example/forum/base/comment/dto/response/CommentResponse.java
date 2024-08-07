package com.example.forum.base.comment.dto.response;

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
    private boolean isAuthor;
    private Long boardId;
    private String commentProfileImgUrl;

    @Builder
    public CommentResponse(Long id, String nickname, String content, LocalDateTime createDate, boolean isAuthor, Long boardId, String commentProfileImgUrl) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createDate = createDate;
        this.isAuthor = isAuthor;
        this.boardId = boardId;
        this.commentProfileImgUrl = commentProfileImgUrl;
    }
}