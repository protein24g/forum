package com.example.forum.user.dto.response;

import com.example.forum.board.comment.dto.response.CommentResponse;
import com.example.forum.board.freeboard.dto.response.BoardResponse;
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
    private Page<BoardResponse> boards;
    private Page<CommentResponse> comments;

    @Builder
    public UserResponse(Long userId, String nickname, LocalDateTime createDate, boolean isActive,
                        Page<BoardResponse> boards, Page<CommentResponse> comments){
        this.userId = userId;
        this.nickname = nickname;
        this.createDate = createDate;
        this.isActive = isActive;
        this.boards = boards;
        this.comments = comments;
    }
}
