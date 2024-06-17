package com.example.forum.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String fileName;

    private String filePath;

    private LocalDateTime createDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserImage(String originalName, String fileName, String filePath, LocalDateTime createDate){
        this.originalName = originalName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.createDate = createDate;
    }

    public void setUser(User user){
        this.user = user;
    }
}

