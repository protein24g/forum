    package com.example.forum.user.entity;

    import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
    import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Getter
    @NoArgsConstructor
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

        public enum Role{ADMIN, USER}
        @Enumerated(EnumType.STRING) // 권한을 문자열로 db에 저장
        private Role role;

        @Column(name = "is_active")
        private boolean isActive = true; // 사용자의 활성화 상태를 나타냅니다.

        @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
        private UserImage userImage;

        @Builder
        public User(String nickname, String loginId, String loginPw, LocalDateTime createDate,
                    int age, User.Gender gender, User.Role role, boolean isActive){
            this.nickname = nickname;
            this.loginId = loginId;
            this.loginPw = loginPw;
            this.createDate = createDate;
            this.age = age;
            this.gender = gender;
            this.role = role;
            this.isActive = isActive;
        }

        public void addUserImage(UserImage userImage){
            this.userImage = userImage;
            userImage.setUser(this);
        }

        public boolean getActive() {
            return this.isActive;
        }

        public void setActive(boolean active) {
            this.isActive = active;
        }
    }
