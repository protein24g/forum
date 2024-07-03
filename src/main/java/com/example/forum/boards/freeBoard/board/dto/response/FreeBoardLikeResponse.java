package com.example.forum.boards.freeBoard.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FreeBoardLikeResponse {
    private boolean res;
    private int likes;

    @Builder
    public FreeBoardLikeResponse(boolean res, int likes){
        this.res = res;
        this.likes = likes;
    }
}
