package com.example.forum.user.profile.guestbook.entity;

import com.example.forum.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class GuestBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private LocalDateTime createDate;

    private Long targetId;

    @Builder
    public GuestBook(String content, LocalDateTime createDate, Long targetId){
        this.content = content;
        this.createDate = createDate;
        this.targetId = targetId;
    }

    public void setUser(User user){
        this.user = user;
    }
}
