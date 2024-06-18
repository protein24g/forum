package com.example.forum.user.dto.response;

import com.example.forum.base.comment.dto.response.CommentResponse;
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
    private Page<FreeBoardResponse> freeBoards;
    private int freeBoards_size;
    private int questionBoards_size;
    private Page<CommentResponse> comments;
    private int comments_size;
    private String boardListImages;

    @Builder
    public UserResponse(Long userId, String nickname, LocalDateTime createDate, boolean isActive,
                        Page<FreeBoardResponse> freeBoards, int freeBoards_Size,
                        Page<CommentResponse> comments, int comments_size, String boardListImages){
        this.userId = userId;
        this.nickname = nickname;
        this.createDate = createDate;
        this.isActive = isActive;
        this.freeBoards = freeBoards;
        this.freeBoards_size = freeBoards_Size;
        this.comments = comments;
        this.comments_size = comments_size;
        this.boardListImages = boardListImages;
    }
}
