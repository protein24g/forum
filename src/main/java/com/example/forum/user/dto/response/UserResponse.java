package com.example.forum.user.dto.response;

import com.example.forum.boards.freeboard.comment.dto.response.FreeBoardCommentResponse;
import com.example.forum.boards.freeboard.board.dto.response.FreeBoardResponse;
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
    private Page<FreeBoardCommentResponse> comments;

    @Builder
    public UserResponse(Long userId, String nickname, LocalDateTime createDate, boolean isActive,
                        Page<FreeBoardResponse> boards, Page<FreeBoardCommentResponse> comments){
        this.userId = userId;
        this.nickname = nickname;
        this.createDate = createDate;
        this.isActive = isActive;
        this.boards = boards;
        this.comments = comments;
    }
}
