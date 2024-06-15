package com.example.forum.boards.questionBoard.board.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuestionBoardSearch {
    private String keyword;
    private int page;
    private String option;
    private int pageSize;
}
