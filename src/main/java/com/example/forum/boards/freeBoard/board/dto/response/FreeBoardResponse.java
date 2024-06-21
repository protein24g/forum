package com.example.forum.boards.freeBoard.board.dto.response;

import com.example.forum.base.comment.dto.response.CommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class FreeBoardResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Page<CommentResponse> commentResponses;
    private int commentCount;
    private int view;
    private List<String> images;
    private boolean hasImage;

    @Builder
    public FreeBoardResponse(Long id, String nickname, String title, String content, LocalDateTime createDate,
                             Page<CommentResponse> commentResponses, int commentCount, int view,
                             List<String> images, boolean hasImage) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.commentResponses = commentResponses;
        this.commentCount = commentCount;
        this.view = view;
        this.images = images;
        this.hasImage = hasImage;
    }
}
