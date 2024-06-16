package com.example.forum.boards.questionBoard.board.dto.response;

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
public class QuestionBoardResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Page<CommentResponse> commentResponses;
    private int commentCount;
    private int view;
    private String thumbnail;
    private List<String> images;
    private boolean hasImage;

    @Builder
    public QuestionBoardResponse(Long id, String nickname, String title, String content, LocalDateTime createDate,
                             Page<CommentResponse> commentResponses, int commentCount, int view,
                             String thumbnail, List<String> images, boolean hasImage) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.commentResponses = commentResponses;
        this.commentCount = commentCount;
        this.view = view;
        this.thumbnail = thumbnail;
        this.images = images;
        this.hasImage = hasImage;
    }
}
