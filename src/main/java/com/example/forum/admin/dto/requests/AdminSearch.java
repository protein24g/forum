package com.example.forum.admin.dto.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminSearch {
    private String keyword;
    private int page;
    private String option;
}
