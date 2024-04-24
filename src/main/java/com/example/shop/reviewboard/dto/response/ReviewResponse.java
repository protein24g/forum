package com.example.shop.reviewboard.dto.response;

import com.example.shop.comment.dto.response.CommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ReviewResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private List<CommentResponse> commentResponses;

    @Builder
    public ReviewResponse(Long id, String nickname, String title, String content, LocalDateTime createDate, List<CommentResponse> commentResponses){
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.commentResponses = commentResponses;
    }
}
