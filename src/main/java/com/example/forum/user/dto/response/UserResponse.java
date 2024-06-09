package com.example.forum.user.dto.response;

import com.example.forum.boards.freeBoard.comment.dto.response.FreeBoardCommentResponse;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserResponse {
    private Long userId;
    private String nickname;
    private LocalDateTime createDate;
    private boolean isActive;
    private Page<FreeBoardResponse> boards;
    private int boards_size;
    private Page<FreeBoardCommentResponse> comments;
    private int comments_size;

    @Builder
    public UserResponse(Long userId, String nickname, LocalDateTime createDate, boolean isActive,
                        Page<FreeBoardResponse> boards, int boards_size, Page<FreeBoardCommentResponse> comments, int comments_size){
        this.userId = userId;
        this.nickname = nickname;
        this.createDate = createDate;
        this.isActive = isActive;
        this.boards = boards;
        this.boards_size = boards_size;
        this.comments = comments;
        this.comments_size = comments_size;
    }
}
