package com.example.shop.user.service;

import com.example.shop.user.dto.requests.JoinRequest;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void join(JoinRequest joinRequest) {
        userRepository.findByLoginId(joinRequest.getLoginId())
                .ifPresentOrElse(
                        user -> {
                            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
                        },
                        () -> {
                            userRepository.save(User.builder()
                                    .nickname(joinRequest.getNickname())
                                    .loginId(joinRequest.getLoginId())
                                    .loginPw(passwordEncoder.encode(joinRequest.getLoginPw()))
                                    .createDate(LocalDateTime.now())
                                    .age(joinRequest.getAge())
                                    .gender(joinRequest.getGender())
                                    .address(joinRequest.getAddress())
                                    .role(User.Role.USER)
                                    .build());
                        }
                );
    }
}
