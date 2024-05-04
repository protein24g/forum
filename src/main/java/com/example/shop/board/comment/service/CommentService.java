package com.example.shop.board.comment.service;

import com.example.shop.board.comment.dto.requests.CommentRequest;
import com.example.shop.board.comment.dto.response.CommentResponse;
import com.example.shop.board.comment.entity.Comment;
import com.example.shop.board.comment.repository.CommentRepository;
import com.example.shop.board.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.board.qnaboard.repository.QnaRepository;
import com.example.shop.board.reviewboard.entity.Review;
import com.example.shop.board.reviewboard.repository.ReviewRepository;
import com.example.shop.user.dto.CustomUserDetails;
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
    private final QnaRepository qnaRepository;
    private final ReviewRepository reviewRepository;

    // C(Create)
    public CommentResponse createReview(Long boardNum, CommentRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 게시글
            Review review = reviewRepository.findById(boardNum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

            Comment comment = Comment.builder()
                    .user(user)
                    .review(review)
                    .content(dto.getContent())
                    .createDate(LocalDateTime.now())
                    .build();
            commentRepository.save(comment);
            review.addComment(comment);
            return CommentResponse.builder()
                    .id(comment.getId())
                    .nickname(comment.getUser().getActive() ? comment.getUser().getNickname() : "탈퇴한 사용자")
                    .content(comment.getContent())
                    .createDate(comment.getCreateDate())
                    .isAuthor(false)
                    .build();
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요.");
        }
    }

    public void createQna(Long boardNum, CommentRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 게시글
            QuestionAndAnswer questionAndAnswer = qnaRepository.findById(boardNum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

            if(user.getRole().equals(User.Role.ADMIN)){ // 관리자가 댓글을 달면 답변완료 처리
                questionAndAnswer.setCompleted(true);
                Comment comment = Comment.builder()
                        .user(user)
                        .questionAndAnswer(questionAndAnswer)
                        .content(dto.getContent())
                        .createDate(LocalDateTime.now())
                        .build();
                commentRepository.save(comment);
                questionAndAnswer.addComment(comment);
            }else{
                throw new IllegalArgumentException("관리자만 답변 작성 가능합니다.");
            }

        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요.");
        }
    }

    // R(Read)
    public Page<CommentResponse> findAllComment(Long reviewId, int commentP) {
        String username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            username = customUserDetails.getUsername();
        } else { username = ""; }
        Pageable pageable = PageRequest.of(commentP, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Comment> comments = commentRepository.findByReviewId(reviewId, pageable);
        return comments.map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getUser().getActive() ? comment.getUser().getNickname() : "탈퇴한 사용자")
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .isAuthor(comment.getUser().getNickname().equals(username) ? true : false)
                .build());
    }

    // U(Update)
    public void updateComment(Long commentNum, CommentRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 댓글
            Comment comment = commentRepository.findById(commentNum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 입니다."));

            if(comment.getReview() != null){ // 리뷰의 댓글이면
                if(comment.getUser().getId().equals(user.getId())){
                    comment.setContent(dto.getContent());
                    commentRepository.save(comment);
                }else{
                    throw new IllegalArgumentException("댓글 작성자만 수정 d가능합니다.");
                }
            }else{ // QnA의 댓글이면
                if(comment.getUser().getId().equals(user.getId())){
                    comment.setContent(dto.getContent());
                    commentRepository.save(comment);
                }else{
                    throw new IllegalArgumentException("댓글 작성자만 수정 가능합니다.");
                }
            }
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요.");
        }
    }

    // D(Delete)
    public String deleteComment(Long commentNum){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 댓글
            Comment comment = commentRepository.findById(commentNum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 입니다."));

            if (comment.getReview() != null) { // 리뷰의 댓글이면
                if (comment.getUser().getId().equals(user.getId())) {
                    commentRepository.delete(comment);
                    return "/review/" + comment.getReview().getId();
                } else {
                    throw new IllegalArgumentException("댓글 작성자만 삭제 가능합니다.");
                }
            } else { // QnA의 댓글이면
                if (comment.getUser().getId().equals(user.getId())) {
                    commentRepository.delete(comment);
                    return "/qna/" + comment.getQuestionAndAnswer().getId();
                } else {
                    throw new IllegalArgumentException("댓글 작성자만 삭제 가능합니다.");
                }
            }
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요.");
        }
    }
}
