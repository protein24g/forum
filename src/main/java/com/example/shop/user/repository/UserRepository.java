package com.example.shop.user.repository;

import com.example.shop.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByNickname(String nickname);
    List<User> findByNicknameContaining(String keyword);
}
