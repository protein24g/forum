package com.example.shop.board.freeboard.dto.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardRequest {
    private String title;

    private String content;
}