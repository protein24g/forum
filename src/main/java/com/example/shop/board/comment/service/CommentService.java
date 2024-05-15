package com.example.shop.board.comment.service;

import com.example.shop.board.freeboard.entity.Board;
import com.example.shop.board.comment.dto.requests.CommentRequest;
import com.example.shop.board.comment.dto.response.CommentResponse;
import com.example.shop.board.comment.entity.Comment;
import com.example.shop.board.comment.repository.CommentRepository;
import com.example.shop.board.freeboard.repository.BoardRepository;
import com.example.shop.user.dto.requests.CustomUserDetails;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // C(Create)
    public void createCommentForBoard(Long boardId, CommentRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 게시글
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

            Comment comment = Comment.builder()
                    .user(user)
                    .board(board)
                    .content(dto.getContent())
                    .createDate(LocalDateTime.now())
                    .build();
            commentRepository.save(comment);
            board.addComment(comment);
        }
    }

    // R(Read)
    public Page<CommentResponse> getCommentsForUser(Long userId, int page) {
        String username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            username = customUserDetails.getUsername();
        } else username = "";

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Comment> comments = commentRepository.findByUserId(userId, pageable);
        return comments.map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getUser().getActive() ? comment.getUser().getNickname() : "탈퇴한 사용자")
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .isAuthor(comment.getUser().getNickname().equals(username))
                .boardId(comment.getBoard().getId())
                .build());
    }

    public Page<CommentResponse> getCommentsForBoard(Long boardId, int page) {
        String username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            username = customUserDetails.getUsername();
        } else username = "";

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Comment> comments = commentRepository.findByBoardId(boardId, pageable);
        return comments.map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getUser().getActive() ? comment.getUser().getNickname() : "탈퇴한 사용자")
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .isAuthor(comment.getUser().getNickname().equals(username))
                .build());
    }

    // U(Update)
    public void updateComment(Long commentId, CommentRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 댓글
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 입니다."));

            if(comment.getBoard() != null){ // 리뷰의 댓글이면
                if(comment.getUser().getId().equals(user.getId())){
                    comment.setContent(dto.getContent());
                    commentRepository.save(comment);
                }else{
                    throw new IllegalArgumentException("댓글 작성자만 수정 가능합니다.");
                }
            }else{ // QnA의 댓글이면
                if(comment.getUser().getId().equals(user.getId())){
                    comment.setContent(dto.getContent());
                    commentRepository.save(comment);
                }else{
                    throw new IllegalArgumentException("댓글 작성자만 수정 가능합니다.");
                }
            }
        }
    }

    // D(Delete)
    public void deleteComment(Long commentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 댓글
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 입니다."));

            if (comment.getBoard() != null) { // 리뷰의 댓글이면
                if (comment.getUser().getId().equals(user.getId())) {
                    commentRepository.delete(comment);
                } else {
                    throw new IllegalArgumentException("댓글 작성자만 삭제 가능합니다.");
                }
            } else { // QnA의 댓글이면
                if (comment.getUser().getId().equals(user.getId())) {
                    commentRepository.delete(comment);
                } else {
                    throw new IllegalArgumentException("댓글 작성자만 삭제 가능합니다.");
                }
            }
        }
    }
}
