    package com.example.forum.user.entity;

    import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
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

        private String certificate;

        private String career;

        @Column(name = "create_date", updatable = false)
        private LocalDateTime createDate;

        public enum Role{ADMIN, USER}
        @Enumerated(EnumType.STRING) // 권한을 문자열로 db에 저장
        private Role role;

        @Column(name = "is_active")
        private boolean isActive = true; // 사용자의 활성화 상태

        @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
        private UserImage userImage;

        // CascadeType.REMOVE : 부모 Entity 삭제시 자식 Entity 들도 삭제
        // orphanRemoval = true : 부모 엔티티와의 관계가 끊어진 자식 엔티티들을 자동으로 삭제
        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
        private List<FreeBoard> freeBoards = new ArrayList<>();

        @Builder
        public User(String nickname, String loginId, String loginPw,
                    String certificate, String career,
                    LocalDateTime createDate, User.Role role, boolean isActive){
            this.nickname = nickname;
            this.loginId = loginId;
            this.loginPw = loginPw;
            this.certificate = certificate;
            this.career = career;
            this.createDate = createDate;
            this.role = role;
            this.isActive = isActive;
        }

        public void addBoard(FreeBoard freeBoard){
            this.freeBoards.add(freeBoard);
            freeBoard.setBoard(this);
        }

        public void setUserImage(UserImage userImage){
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
