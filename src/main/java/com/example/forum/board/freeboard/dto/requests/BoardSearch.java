package com.example.forum.board.freeboard.dto.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardSearch {
    private String keyword;
    private int page;
    private String option;
    private int pageSize;
}
