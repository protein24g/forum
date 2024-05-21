package com.example.forum.boards.freeboard.board.dto.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FreeBoardRequest {
    private String title;

    private String content;
}
