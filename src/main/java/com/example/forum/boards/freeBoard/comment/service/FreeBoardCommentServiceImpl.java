package com.example.forum.boards.freeBoard.comment.service;

import com.example.forum.base.comment.dto.request.CommentRequest;
import com.example.forum.base.comment.dto.response.CommentResponse;
import com.example.forum.base.comment.service.CommentService;
import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
import com.example.forum.boards.freeBoard.comment.repository.FreeBoardCommentRepository;
import com.example.forum.boards.freeBoard.board.repository.FreeBoardRepository;
import com.example.forum.user.auth.dto.requests.CustomUserDetails;
import com.example.forum.user.auth.repository.UserAuthRepository;
import com.example.forum.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 자유 게시판 댓글 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FreeBoardCommentServiceImpl implements CommentService {
    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final UserAuthRepository userAuthRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final AuthenticationService authenticationService;

    /**
     * 게시글에 댓글 작성
     *
     * @param boardId 게시글 ID
     * @param dto     댓글 요청 DTO
     */
    @Override
    public void createCommentForBoard(Long boardId, CommentRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            // 유저
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 게시글
            FreeBoard board = freeBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

            FreeBoardComment freeBoardComment = FreeBoardComment.builder()
                    .user(user)
                    .freeBoard(board)
                    .content(dto.getContent())
                    .createDate(LocalDateTime.now())
                    .build();
            freeBoardCommentRepository.save(freeBoardComment);
            board.addComment(freeBoardComment);
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 특정 사용자의 댓글 목록 조회
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @return 댓글 페이지 응답 DTO
     */
    @Override
    public Page<CommentResponse> getCommentsForUser(Long userId, int page) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
            Page<FreeBoardComment> comments = freeBoardCommentRepository.findByUserId(userId, pageable);
            return comments.map(comment -> CommentResponse.builder()
                    .id(comment.getId())
                    .nickname(comment.getUser().getActive() ? comment.getUser().getNickname() : "탈퇴한 사용자")
                    .content(comment.getContent())
                    .createDate(comment.getCreateDate())
                    .isAuthor(comment.getUser().getNickname().equals(customUserDetails.getUsername()))
                    .boardId(comment.getFreeBoard().getId())
                    .build());
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 특정 게시글의 댓글 목록 조회
     *
     * @param boardId 게시글 ID
     * @param page    페이지 번호
     * @return 댓글 페이지 응답 DTO
     */
    @Override
    public Page<CommentResponse> getCommentsForBoard(Long boardId, int page) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoardComment> comments = freeBoardCommentRepository.findByFreeBoardId(boardId, pageable);
        return comments.map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getUser().getActive() ? comment.getUser().getNickname() : "탈퇴한 사용자")
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .isAuthor(comment.getUser().getNickname().equals((customUserDetails != null) ? customUserDetails.getUsername() : ""))
                .build());
    }

    /**
     * 사용자가 댓글 단 게시글 목록을 조회
     *
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @return 게시글 페이지 응답 DTO
     */
    @Override
    public Page<FreeBoardResponse> getFreeBoardByUserComments(Long userId, int page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoard> boards = freeBoardCommentRepository.getFreeBoardByUserComments(userId, pageable);
        return boards
                .map(board -> FreeBoardResponse.builder()
                        .id(board.getId())
                        // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .content(board.getContent())
                        .createDate(board.getCreateDate())
                        .commentCount(freeBoardCommentRepository.getPostCommentCount(board.getId()))
                        .view(board.getView())
                        .hasImage((board.getImages().size() >= 1 ? true : false))
                        .build());
    }

    /**
     * 댓글 수정
     *
     * @param commentId 댓글 ID
     * @param dto       댓글 요청 DTO
     */
    @Override
    public void updateComment(Long commentId, CommentRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            // 유저
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 댓글
            FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 입니다."));

            if(freeBoardComment.getUser().getId().equals(user.getId())){
                freeBoardComment.setContent(dto.getContent());
                freeBoardCommentRepository.save(freeBoardComment);
            }else{
                throw new IllegalArgumentException("댓글 작성자만 수정 가능합니다.");
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 ID
     */
    @Override
    public void deleteComment(Long commentId) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            // 유저
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 댓글
            FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 입니다."));

            if (freeBoardComment.getUser().getId().equals(user.getId())) {
                freeBoardCommentRepository.delete(freeBoardComment);
            } else {
                throw new IllegalArgumentException("댓글 작성자만 삭제 가능합니다.");
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }
}
