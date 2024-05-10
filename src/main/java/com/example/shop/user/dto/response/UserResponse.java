package com.example.shop.user.dto.response;

import com.example.shop.board.comment.dto.response.CommentResponse;
import com.example.shop.board.freeboard.dto.response.BoardResponse;
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
    private String nickname;
    private LocalDateTime createDate;
    private boolean isActive;
    private Page<BoardResponse> boards;
    private Page<CommentResponse> comments;

    @Builder
    public UserResponse(String nickname, LocalDateTime createDate, boolean isActive,
                        Page<BoardResponse> boards, Page<CommentResponse> comments){
        this.nickname = nickname;
        this.createDate = createDate;
        this.isActive = isActive;
        this.boards = boards;
        this.comments = comments;
    }
}
