package com.example.forum.user.repository;

import com.example.forum.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId); // 회원가입 아이디 중복 조회
    boolean existsByNickname(String nickname); // 회원가입 닉네임 중복 조회
    Optional<User> findByLoginId(String loginId); // 로그인 아이디로 회원 조회
    Page<User> findAll(Pageable pageable);
    Page<User> findByNicknameContaining(String keyword, Pageable pageable); // 닉네임 키워드 검색
    Page<User> existsByLoginIdContaining(String keyword, Pageable pageable); // 아이디 키워드 검색

    @Query("SELECT COUNT(b) FROM FreeBoard b WHERE b.user.id = :userId")
    int getUserPostCount(Long userId);

    @Query("SELECT COUNT(c) FROM FreeBoardComment c WHERE c.user.id = :userId")
    int getUserCommentCount(Long userId);
}
