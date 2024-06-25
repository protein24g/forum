package com.example.forum.user.profile.service;

import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
import com.example.forum.user.auth.dto.requests.CustomUserDetails;
import com.example.forum.user.auth.dto.response.UserResponse;
import com.example.forum.user.auth.repository.UserAuthRepository;
import com.example.forum.user.entity.User;
import com.example.forum.user.profile.guestbook.dto.request.GuestBookRequest;
import com.example.forum.user.profile.guestbook.dto.response.GuestBookResponse;
import com.example.forum.user.profile.guestbook.entity.GuestBook;
import com.example.forum.user.profile.guestbook.repository.GuestBookRepository;
import com.example.forum.user.profile.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional

/**
 * 사용자 관리 서비스 클래스
 */
public class UserProfileService {
    private final UserAuthRepository userAuthRepository;
    private final UserProfileRepository userProfileRepository;
    private final FreeBoardServiceImpl freeBoardServiceImpl;
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;
    private final AuthenticationService authenticationService;
    private final GuestBookRepository guestBookRepository;

    /**
     * 내 정보 조회
     *
     * @return 내 정보 반환
     */
    public UserResponse getMyInfo() {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            return UserResponse.builder()
                    .nickname(user.getNickname())
                    .freeBoards_Size(userProfileRepository.getUserFreePostCount(user.getId()))
                    .comments_size(userProfileRepository.getUserFreeCommentCount(user.getId()))
                    .profileImage((user.getUserImage() != null) ? user.getUserImage().getFileName() : null)
                    .build();
        } else {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

    /**
     * 특정 사용자 정보 조회
     *
     * @return 내 정보 반환
     */
    public UserResponse getUserInfo(String nickname) {
        User user = userAuthRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return UserResponse.builder()
                .nickname(user.getNickname())
                .createDate(user.getCreateDate())
                .freeBoards_Size(userProfileRepository.getUserFreePostCount(user.getId()))
                .comments_size(userProfileRepository.getUserFreeCommentCount(user.getId()))
                .certificate(user.getCertificate())
                .career(user.getCareer())
                .profileImage((user.getUserImage() != null) ? user.getUserImage().getFileName() : null)
                .build();
    }

    /**
     * 마이페이지에서 작성한 게시글 또는 댓글 조회
     *
     * @param id   게시글 또는 댓글 식별자 ("myBoards" 또는 "myComments")
     * @param page 페이지 번호
     * @return 게시글 또는 댓글 목록 응답 페이지 DTO
     */
    public Page<FreeBoardResponse> myPageBoards(String id, int page) { // id에 따른 내가 쓴 글, 댓글 단 글 불러오기
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            Page<FreeBoardResponse> freeBoardResponses;
            User user = userAuthRepository.findById(customUserDetails.getId())
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

    /**
     * 특정 유저 상세 페이지에서 작성한 게시글 또는 댓글 조회
     *
     * @param id   게시글 또는 댓글 식별자 ("myBoards" 또는 "myComments")
     * @param page 페이지 번호
     * @return 게시글 또는 댓글 목록 응답 페이지 DTO
     */
    public Page<FreeBoardResponse> userInfoPageBoards(String nickname, String id, int page) { // id에 따른 작성 글, 댓글 단 글 불러오기
        Page<FreeBoardResponse> freeBoardResponses;
        User user = userAuthRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        if(id.equals("myBoards")){
            freeBoardResponses = freeBoardServiceImpl.getBoardsForUser(user.getId(), page);
        }else{
            freeBoardResponses = freeBoardCommentServiceImpl.getFreeBoardByUserComments(user.getId(), page);
        }
        return freeBoardResponses;
    }

    /**
     * 특정 사용자 페이지 방명록 작성
     *
     * @param nickname 사용자 닉네임
     * @param dto      방명록 작성 객체
     * @return
     */
    public GuestBookResponse createGuestBook(String nickname, GuestBookRequest dto){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

            User target = userAuthRepository.findByNickname(nickname)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

            if(user.getId().equals(target.getId())){
                throw new IllegalArgumentException("자기 자신에게는 방명록 작성이 불가능합니다");
            }

            GuestBook guestBook = GuestBook.builder()
                    .content(dto.getContent())
                    .createDate(LocalDateTime.now())
                    .targetId(target.getId())
                    .build();
            guestBookRepository.save(guestBook);
            guestBook.setUser(user);

            return GuestBookResponse.builder()
                    .content(guestBook.getContent())
                    .createDate(guestBook.getCreateDate())
                    .build();
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 특정 사용자 페이지 방명록 글 목록 페이징 조회
     *
     * @param nickname 특정 사용자 닉네임
     * @param page     페이지
     * @param size     페이지당 보여줄 개수
     * @return
     */
    public Page<GuestBookResponse> userInfoGuestBooks(String nickname, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<GuestBook> guestBooks = guestBookRepository.findByNickname(nickname, pageable);

        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        return guestBooks.map(guestBook -> GuestBookResponse.builder()
                .content(guestBook.getContent())
                .createDate(guestBook.getCreateDate())
                .isWriter(customUserDetails != null && (customUserDetails.getId().equals(guestBook.getUser().getId())))
                .build());
    }
}
