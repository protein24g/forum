package com.example.shop.user.repository;

import com.example.shop.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId); // 회원가입 아이디 중복 조회
    Optional<User> findByNickname(String nickname); // 회원가입 닉네임 중복 조회
    Page<User> findAll(Pageable pageable);
    Page<User> findByNicknameContaining(String keyword, Pageable pageable); // 닉네임 키워드 검색
    Page<User> findByLoginIdContaining(String keyword, Pageable pageable); // 아이디 키워드 검색
}
