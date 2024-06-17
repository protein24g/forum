package com.example.forum.user.dto.response;

import com.example.forum.base.comment.dto.response.CommentResponse;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.questionBoard.board.dto.response.QuestionBoardResponse;
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
    private Page<QuestionBoardResponse> questionBoards;
    private int questionBoards_size;
    private Page<CommentResponse> comments;
    private int comments_size;
    private String userImage_file_name;

    @Builder
    public UserResponse(Long userId, String nickname, LocalDateTime createDate, boolean isActive,
                        Page<FreeBoardResponse> freeBoards, int freeBoards_Size,
                        Page<QuestionBoardResponse> questionBoards, int questionBoards_size,
                        Page<CommentResponse> comments, int comments_size, String userImage_file_name){
        this.userId = userId;
        this.nickname = nickname;
        this.createDate = createDate;
        this.isActive = isActive;
        this.freeBoards = freeBoards;
        this.freeBoards_size = freeBoards_Size;
        this.questionBoards = questionBoards;
        this.questionBoards_size = questionBoards_size;
        this.comments = comments;
        this.comments_size = comments_size;
        this.userImage_file_name = userImage_file_name;
    }
}
