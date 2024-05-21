package com.example.forum.boards.freeboard.board.dto.response;

import com.example.forum.boards.freeboard.comment.dto.response.FreeBoardCommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class FreeBoardResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Page<FreeBoardCommentResponse> commentResponses;
    private int commentCount;
    private int view;

    @Builder
    public FreeBoardResponse(Long id, String nickname, String title, String content, LocalDateTime createDate,
                             Page<FreeBoardCommentResponse> commentResponses, int commentCount, int view){
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
