package com.example.shop.comment.service;

import com.example.shop.comment.dto.requests.CommentRequest;
import com.example.shop.comment.entity.Comment;
import com.example.shop.comment.repository.CommentRepository;
import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.qnaboard.repository.QnaRepository;
import com.example.shop.reviewboard.entity.Review;
import com.example.shop.reviewboard.repository.ReviewRepository;
import com.example.shop.user.dto.CustomUserDetails;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public void createQna(Long boardNum, CommentRequest commentRequest) {
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
            }

            Comment comment = Comment.builder()
                    .user(user)
                    .questionAndAnswer(questionAndAnswer)
                    .content(commentRequest.getContent())
                    .createDate(LocalDateTime.now())
                    .build();
            commentRepository.save(comment);
            questionAndAnswer.addComment(comment);
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요.");
        }
    }

    public void createReview(Long boardNum, CommentRequest commentRequest) {
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
                    .content(commentRequest.getContent())
                    .createDate(LocalDateTime.now())
                    .build();
            commentRepository.save(comment);
            review.addComment(comment);
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요.");
        }
    }
}
