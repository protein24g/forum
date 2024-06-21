package com.example.forum.user.service;

import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
import com.example.forum.user.dto.requests.CustomUserDetails;
import com.example.forum.user.dto.response.UserResponse;
import com.example.forum.user.entity.User;
import com.example.forum.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional

/**
 * 사용자 관리 서비스 클래스
 */
public class UserService {
    private final UserRepository userRepository;
    private final FreeBoardServiceImpl freeBoardServiceImpl;
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;
    private final AuthenticationService authenticationService;

    /**
     * 내 정보 조회
     *
     * @return 내 정보 반환
     */
    public UserResponse getMyInfo() {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            return UserResponse.builder()
                    .nickname(user.getNickname())
                    .freeBoards_Size(userRepository.getUserPostCount(user.getId()))
                    .comments_size(userRepository.getUserCommentCount(user.getId()))
                    .profileImage((user.getUserImage() != null) ? user.getUserImage().getFileName() : null)
                    .build();
        } else {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

    /**
     * 마이페이지에서 작성한 게시글 또는 댓글 조회
     *
     * @param id   게시글 또는 댓글 종류 식별자 ("myBoards" 또는 "myComments")
     * @param page 페이지 번호
     * @return 게시글 또는 댓글 목록 응답 페이지 DTO
     */
    public Page<FreeBoardResponse> myPageBoards(String id, int page) { // id에 따른 내가 쓴 글, 댓글 단 글 불러오기
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            Page<FreeBoardResponse> freeBoardResponses;
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

            if(id.equals("myBoards")){
                freeBoardResponses = freeBoardServiceImpl.getBoardsForUser(user.getId(), page);
            }else{
                freeBoardResponses = freeBoardCommentServiceImpl.getFreeBoardByUserComments(user.getId(), page);
            }
            return freeBoardResponses;
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }
}
