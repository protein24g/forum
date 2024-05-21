package com.example.forum.boards.freeboard.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class FreeBoardCommentResponse {
    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createDate;
    private boolean isAuthor;
    private Long boardId;

    @Builder
    public FreeBoardCommentResponse(Long id, String nickname, String content, LocalDateTime createDate, boolean isAuthor, Long boardId){
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createDate = createDate;
        this.isAuthor = isAuthor;
        this.boardId = boardId;
    }
}
