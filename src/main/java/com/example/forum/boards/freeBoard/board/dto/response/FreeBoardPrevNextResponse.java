package com.example.forum.boards.freeBoard.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FreeBoardPrevNextResponse {
    private Long prevId;
    private String prevTitle;
    private Long nextId;
    private String nextTitle;

    @Builder
    public FreeBoardPrevNextResponse(Long prevId, String prevTitle, Long nextId, String nextTitle) {
        this.prevId = prevId;
        this.prevTitle = prevTitle;
        this.nextId = nextId;
        this.nextTitle = nextTitle;
    }
}
