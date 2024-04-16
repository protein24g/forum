package com.example.shop.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
@ToString(exclude = {"", ""}) // 순환 참조를 피하기 위해 ToString에서 제외
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "user_pw", nullable = false)
    private String userPw;

    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "age", nullable = false)
    private int age;

    public enum Gender{MALE, FEMALE}
    @Enumerated(EnumType.STRING) // 성별을 문자열로 db에 저장
    private Gender gender;

    @Column(name = "address", nullable = false)
    private String address;

    public enum Role{ADMIN, USER}
    @Enumerated(EnumType.STRING) // 권한을 문자열로 db에 저장
    private Role role;

    @Builder
    public User(String nickname, String userId, String userPw, LocalDateTime createDate,
                   int age, User.Gender gender, String address, User.Role role){
        this.nickname = nickname;
        this.userId = userId;
        this.userPw = userPw;
        this.createDate = createDate;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.role = role;
    }
}
