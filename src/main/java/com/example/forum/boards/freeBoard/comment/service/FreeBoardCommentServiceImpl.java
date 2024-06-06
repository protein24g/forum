package com.example.forum.boards.freeBoard.comment.service;

import com.example.forum.base.comment.service.CommentService;
import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.comment.dto.requests.FreeBoardCommentRequest;
import com.example.forum.boards.freeBoard.comment.dto.response.FreeBoardCommentResponse;
import com.example.forum.boards.freeBoard.comment.repository.FreeBoardCommentRepository;
import com.example.forum.boards.freeBoard.board.repository.FreeBoardRepository;
import com.example.forum.user.dto.requests.CustomUserDetails;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class FreeBoardCommentServiceImpl implements CommentService {
    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final UserRepository userRepository;
    private final FreeBoardRepository freeBoardRepository;

    // C(Create)
    @Override
    public void createCommentForBoard(Long boardId, FreeBoardCommentRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 게시글
            FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

            FreeBoardComment freeBoardComment = FreeBoardComment.builder()
                    .user(user)
                    .freeBoard(freeBoard)
                    .content(dto.getContent())
                    .createDate(LocalDateTime.now())
                    .build();
            freeBoardCommentRepository.save(freeBoardComment);
            freeBoard.addComment(freeBoardComment);
        }
    }


    // R(Read)
    @Override
    public Page<FreeBoardCommentResponse> getCommentsForUser(Long userId, int page) {
        String username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            username = customUserDetails.getUsername();
        } else username = "";

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoardComment> comments = freeBoardCommentRepository.findByUserId(userId, pageable);
        return comments.map(comment -> FreeBoardCommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getUser().getActive() ? comment.getUser().getNickname() : "탈퇴한 사용자")
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .isAuthor(comment.getUser().getNickname().equals(username))
                .boardId(comment.getFreeBoard().getId())
                .build());
    }

    @Override
    public Page<FreeBoardCommentResponse> getCommentsForBoard(Long boardId, int page) {
        String username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            username = customUserDetails.getUsername();
        } else username = "";

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoardComment> comments = freeBoardCommentRepository.findByFreeBoardId(boardId, pageable);
        return comments.map(comment -> FreeBoardCommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getUser().getActive() ? comment.getUser().getNickname() : "탈퇴한 사용자")
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .isAuthor(comment.getUser().getNickname().equals(username))
                .build());
    }

    @Override
    public Page<FreeBoardResponse> getBoardsByUserComments(User user, int page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoard> boards = freeBoardCommentRepository.getFreeBoardsByUserComments(user, pageable);
        return boards
                .map(board -> FreeBoardResponse.builder()
                        .id(board.getId())
                        // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .content(board.getContent())
                        .createDate(board.getCreateDate())
                        .commentCount(board.getFreeBoardComments().size())
                        .view(board.getView())
                        .hasImage((board.getImages().size() >= 1 ? true : false))
                        .build());
    }

    @Override
    // U(Update)
    public void updateComment(Long commentId, FreeBoardCommentRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
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
        }
    }

    // D(Delete)
    @Override
    public void deleteComment(Long commentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 댓글
            FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 입니다."));

            if (freeBoardComment.getUser().getId().equals(user.getId())) {
                freeBoardCommentRepository.delete(freeBoardComment);
            } else {
                throw new IllegalArgumentException("댓글 작성자만 삭제 가능합니다.");
            }
        }
    }
}
