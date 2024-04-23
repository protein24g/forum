package com.example.shop.qnaboard.dto.response;

import com.example.shop.comment.dto.response.CommentResponse;
import com.example.shop.qnaboard.entity.QuestionAndAnswer;
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

    @Builder
    QnaResponse(Long id, String nickname, String title, String content, LocalDateTime createDate, List<CommentResponse> commentResponses, boolean visibility, boolean completed){
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.commentResponses = commentResponses;
        this.visibility = visibility;
        this.completed = completed;
    }
}
