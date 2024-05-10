package com.example.shop.board.freeboard.dto.response;

import com.example.shop.board.comment.dto.response.CommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Page<CommentResponse> commentResponses;
    private int commentCount;
    private int view;

    @Builder
    public BoardResponse(Long id, String nickname, String title, String content, LocalDateTime createDate,
                          Page<CommentResponse> commentResponses, int commentCount, int view){
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.commentResponses = commentResponses;
        this.commentCount = commentCount;
        this.view = view;
    }
}
