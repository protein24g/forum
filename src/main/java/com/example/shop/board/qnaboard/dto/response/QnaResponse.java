package com.example.shop.board.qnaboard.dto.response;

import com.example.shop.board.comment.dto.response.CommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class QnaResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private List<CommentResponse> commentResponses;
    private boolean visibility;
    private boolean completed;
    private int commentCount;

    @Builder
    public QnaResponse(Long id, String nickname, String title, String content, LocalDateTime createDate, List<CommentResponse> commentResponses, boolean visibility, boolean completed, int commentCount){
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.commentResponses = commentResponses;
        this.visibility = visibility;
        this.completed = completed;
        this.commentCount = commentCount;
    }
}
