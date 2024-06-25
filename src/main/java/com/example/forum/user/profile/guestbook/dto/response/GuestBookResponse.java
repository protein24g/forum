package com.example.forum.user.profile.guestbook.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class GuestBookResponse {
    private String content;
    private LocalDateTime createDate;
    private boolean isWriter;

    @Builder
    public GuestBookResponse(String content, LocalDateTime createDate, boolean isWriter){
        this.content = content;
        this.createDate = createDate;
        this.isWriter = isWriter;
    }
}
