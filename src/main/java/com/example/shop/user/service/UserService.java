package com.example.shop.user.service;

import com.example.shop.board.comment.dto.response.CommentResponse;
import com.example.shop.board.comment.service.CommentService;
import com.example.shop.board.reviewboard.dto.response.ReviewResponse;
import com.example.shop.board.reviewboard.service.ReviewService;
import com.example.shop.user.dto.requests.JoinRequest;
import com.example.shop.user.dto.response.UserResponse;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ReviewService reviewService;
    private final CommentService commentService;

    public void join(JoinRequest joinRequest) {
        userRepository.findByLoginId(joinRequest.getLoginId())
                .ifPresentOrElse(
                        user -> {
                            throw new IllegalArgumentException("이미 사용중인 아이디 입니다.");
                        },
                        () -> {
                            userRepository.findByNickname(joinRequest.getNickname())
                                .ifPresentOrElse(
                                    user -> {
                                        throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");
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
                                            .isActive(true)
                                            .build());
                                    }
                                );
                        }
                );
    }

    public boolean isLoginIdAvailable(String loginId) {
        return userRepository.findByLoginId(loginId).isPresent();
    }

    public boolean isNicknameAvailable(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    // C(Create)

    // R(Read)
    public UserResponse getUserDetail(Long userId, int page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(user.getActive()){
            return UserResponse.builder()
                    .nickname(user.getNickname())
                    .createDate(user.getCreateDate())
                    .reviews(reviewService.getReviewsForUser(userId, page))
                    .comments(commentService.getCommentsForUser(userId, page))
                    .isActive(user.getActive())
                    .build();
        }else{
            throw new IllegalArgumentException("탈퇴한 사용자");
        }
    }

    // U(Update)

    // D(Delete)
}
