package com.example.shop.user.repository;

import com.example.shop.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByNickname(String nickname);
    Page<User> findAll(Pageable pageable);
    Page<User> findByNicknameContaining(String keyword, Pageable pageable);
    Page<User> findByLoginIdContaining(String keyword, Pageable pageable);
}
