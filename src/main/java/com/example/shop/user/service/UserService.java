package com.example.shop.user.service;

import com.example.shop.board.admin.dto.response.AdminResponse;
import com.example.shop.board.comment.service.CommentService;
import com.example.shop.board.freeboard.service.BoardService;
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
    private final BoardService boardService;
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
    public UserResponse getUserBoards(Long userId, int page){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(user.getActive()){
            return UserResponse.builder()
                    .nickname(user.getNickname())
                    .createDate(user.getCreateDate())
                    .boards(boardService.getBoardsForUser(userId, page))
                    .isActive(user.getActive())
                    .build();
        }else{
            throw new IllegalArgumentException("탈퇴한 사용자");
        }
    }

    public UserResponse getUserComments(Long userId, int page){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(user.getActive()){
            return UserResponse.builder()
                    .nickname(user.getNickname())
                    .createDate(user.getCreateDate())
                    .comments(commentService.getCommentsForUser(userId, page))
                    .isActive(user.getActive())
                    .build();
        }else{
            throw new IllegalArgumentException("탈퇴한 사용자");
        }
    }

    public List<UserResponse> UsersByKeyword(String keyword){
        List<User> users = userRepository.findByNicknameContaining(keyword);
        return users.stream()
                .map(user -> UserResponse.builder()
                        .userId(user.getId())
                        .nickname(user.getNickname())
                        .build())
                .collect(Collectors.toList());
    }

    public Page<AdminResponse> getAllUsersForAdmin(String keyword, int page, String option){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> users = null;
        if(keyword.length() != 0){ // 키워드가 있으면
            users = userRepository.findByNicknameContaining(keyword, pageable);
        }else{
            users = userRepository.findAll(pageable);
        }
        return users.map(user -> AdminResponse.builder()
                .id(user.getId())
                .role(String.valueOf(user.getRole()))
                .nickname(user.getNickname())
                .userId(user.getLoginId())
                .age(user.getAge())
                .gender(String.valueOf(user.getGender()))
                .createDate(user.getCreateDate())
                .isActive(user.getActive())
                .build());
    }

    // U(Update)

    // D(Delete)
}
