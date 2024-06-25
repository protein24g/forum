package com.example.forum.user.profile.guestbook.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class MyGuestBookResponse {
    private String targetNickname;
    private String content;
    private LocalDateTime createDate;

    @Builder
    public MyGuestBookResponse(String targetNickname, String content, LocalDateTime createDate){
        this.targetNickname = targetNickname;
        this.content = content;
        this.createDate = createDate;
    }
}
