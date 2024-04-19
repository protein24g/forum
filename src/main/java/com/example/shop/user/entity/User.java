package com.example.shop.user.entity;

import com.example.shop.qnaboard.entity.QnA;
import com.example.shop.reviewboard.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@ToString(exclude = {"reviews", "qnAS"}) // 순환 참조를 피하기 위해 ToString 에서 제외
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "login_id", unique = true, nullable = false)
    private String loginId;

    @Column(name = "login_pw", nullable = false)
    private String loginPw;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<QnA> qnAS;

    @Builder
    public User(String nickname, String loginId, String loginPw, LocalDateTime createDate,
                   int age, User.Gender gender, String address, User.Role role){
        this.nickname = nickname;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.createDate = createDate;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.role = role;
        this.reviews = new ArrayList<>();
        this.qnAS = new ArrayList<>();
    }
}
