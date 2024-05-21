package com.example.forum.user.service;

import com.example.forum.board.admin.dto.response.AdminResponse;
import com.example.forum.board.comment.repository.CommentRepository;
import com.example.forum.board.comment.service.CommentService;
import com.example.forum.board.freeboard.dto.response.BoardResponse;
import com.example.forum.board.freeboard.entity.Board;
import com.example.forum.board.freeboard.service.BoardService;
import com.example.forum.user.dto.requests.CustomUserDetails;
import com.example.forum.user.dto.requests.JoinRequest;
import com.example.forum.user.dto.response.UserResponse;
import com.example.forum.user.entity.User;
import com.example.forum.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public Page<AdminResponse> getAllUsersForAdmin(String keyword, int page, String option){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> users = null;
        if(keyword.length() != 0){ // 키워드가 있으면
            switch (option){
                case "1": // 1 닉네임
                    users = userRepository.findByNicknameContaining(keyword, pageable);
                    break;
                case "2": // 2 아이디
                    users = userRepository.findByLoginIdContaining(keyword, pageable);
                    break;
            }
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
                .address(user.getAddress())
                .isActive(user.getActive())
                .build());
    }

    public Page<BoardResponse> myPageBoards(String id, int page) { // id에 따른 내가 쓴 글, 댓글 단 글 불러오기
        Page<BoardResponse> boardResponses = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

            if(id.equals("myboards")){
                boardResponses = boardService.getBoardsForUser(user.getId(), page);
            }else{
                boardResponses = commentService.getBoardsByUserComments(user, page);
            }
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
        return boardResponses;
    }

    // U(Update)

    // D(Delete)
}
