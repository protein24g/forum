package com.example.forum.user.profile.guestbook.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GuestBookRequest {
    private Long id;
    private String content;
}
