package com.example.shop.board.freeboard.dto.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardSearchDTO {
    private String keyword;
    private int page;
    private String option;
}
