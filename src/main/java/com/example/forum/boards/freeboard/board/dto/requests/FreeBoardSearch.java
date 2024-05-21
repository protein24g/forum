package com.example.forum.boards.freeboard.board.dto.requests;

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
