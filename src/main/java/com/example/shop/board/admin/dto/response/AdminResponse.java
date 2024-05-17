package com.example.shop.board.admin.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AdminResponse {
    private Long id;
    private String role;
    private String nickname;
    private String userId;
    private int age;
    private String gender;
    private LocalDateTime createDate;
    private String address;
    private boolean isActive;

    @Builder
    public AdminResponse(Long id, String role, String nickname, String userId,
                  int age, String gender, LocalDateTime createDate, String address, boolean isActive){
        this.id = id;
        this.role = role;
        this.nickname = nickname;
        this.userId = userId;
        this.age = age;
        this.gender = gender;
        this.createDate = createDate;
        this.address = address;
        this.isActive = isActive;
    }
}
