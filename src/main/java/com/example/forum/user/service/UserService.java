package com.example.forum.user.service;

import com.example.forum.admin.dto.response.AdminResponse;
import com.example.forum.base.board.auth.AuthenticationService;
import com.example.forum.base.board.dto.response.BoardResponse;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional

/**
 * 사용자 관리 서비스 클래스
 */
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FreeBoardServiceImpl freeBoardServiceImpl;
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;
    private final AuthenticationService authenticationService;

    /**
     * 회원 가입
     *
     * @param joinRequest 회원 가입 요청 DTO
     */
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
                                            .role(User.Role.USER)
                                            .isActive(true)
                                            .build());
                                    }
                                );
                        }
                );
    }

    /**
     * 로그인 아이디 사용가능 여부
     *
     * @param loginId 로그인 아이디
     * @return 가능 여부
     */
    public boolean isLoginIdAvailable(String loginId) {
        return userRepository.findByLoginId(loginId).isPresent();
    }

    /**
     * 닉네임의 사용가능 여부
     *
     * @param nickname 닉네임
     * @return 가능 여부
     */
    public boolean isNicknameAvailable(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    /**
     * 현재 사용자의 정보 조회
     *
     * @return 사용자 정보 응답 DTO
     */
    public UserResponse getUserInfo() {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            return UserResponse.builder()
                    .nickname(user.getNickname())
                    .boards_size(user.getQuestionBoards().size())
                    .comments_size(user.getQuestionBoardComments().size())
                    .build();
        } else {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

    /**
     * 특정 사용자가 작성한 게시글 목록 조회
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @return 사용자 게시글 응답 DTO
     */
    public UserResponse getUserBoards(Long userId, int page){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(user.getActive()){
            return UserResponse.builder()
                    .nickname(user.getNickname())
                    .createDate(user.getCreateDate())
                    .boards(freeBoardServiceImpl.getBoardsForUser(userId, page))
                    .isActive(user.getActive())
                    .build();
        }else{
            throw new IllegalArgumentException("탈퇴한 사용자");
        }
    }

    /**
     * 특정 사용자가 작성한 댓글 목록 조회
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @return 사용자 댓글 응답 DTO
     */
    public UserResponse getUserComments(Long userId, int page){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(user.getActive()){
            return UserResponse.builder()
                    .nickname(user.getNickname())
                    .createDate(user.getCreateDate())
                    .comments(freeBoardCommentServiceImpl.getCommentsForUser(userId, page))
                    .isActive(user.getActive())
                    .build();
        }else{
            throw new IllegalArgumentException("탈퇴한 사용자");
        }
    }

    /**
     * 관리자용으로 모든 사용자 목록 조회
     *
     * @param keyword 검색 키워드
     * @param page    페이지 번호
     * @param option  검색 옵션
     * @return 사용자 목록 응답 페이지 DTO
     */
    public Page<AdminResponse> getAllUsersForAdmin(String keyword, int page, String option){
        if(authenticationService.isAdmin()){
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
                    .isActive(user.getActive())
                    .build());
        } else {
            throw new IllegalArgumentException("관리자 권한이 필요합니다");
        }
    }

    /**
     * 마이페이지에서 작성한 게시글 또는 댓글 조회
     *
     * @param id   게시글 또는 댓글 종류 식별자 ("myBoards" 또는 "myComments")
     * @param page 페이지 번호
     * @return 게시글 또는 댓글 목록 응답 페이지 DTO
     */
    public Page<BoardResponse> myPageBoards(String id, int page) { // id에 따른 내가 쓴 글, 댓글 단 글 불러오기
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            Page<BoardResponse> boardResponses;
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

            if(id.equals("myBoards")){
                boardResponses = freeBoardServiceImpl.getBoardsForUser(user.getId(), page);
            }else{
                boardResponses = freeBoardCommentServiceImpl.getBoardsByUserComments(user, page);
            }
            return boardResponses;
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }






    }
}
