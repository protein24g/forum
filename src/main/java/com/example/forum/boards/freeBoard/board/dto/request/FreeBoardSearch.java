package com.example.forum.boards.freeBoard.board.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FreeBoardSearch {
    private String keyword;
    private int page;
    private String option;
    private int pageSize;
}
