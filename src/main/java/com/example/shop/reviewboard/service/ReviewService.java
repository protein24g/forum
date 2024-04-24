package com.example.shop.reviewboard.service;

import com.example.shop.comment.dto.response.CommentResponse;
import com.example.shop.comment.entity.Comment;
import com.example.shop.qnaboard.dto.response.QnaResponse;
import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.reviewboard.dto.requests.ReviewRequest;
import com.example.shop.reviewboard.dto.response.ReviewResponse;
import com.example.shop.reviewboard.entity.Review;
import com.example.shop.reviewboard.repository.ReviewRepository;
import com.example.shop.user.dto.CustomUserDetails;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    // C(Create)
    public ReviewResponse create(ReviewRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

            Review review = Review.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .build();
            user.addReview(review);
            reviewRepository.save(review);

            return ReviewResponse.builder()
                    .id(review.getId())
                    .nickname(review.getUser().getNickname())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .createDate(review.getCreateDate())
                    .build();
        }else{
            throw new IllegalArgumentException("로그인 후 이용 가능합니다.");
        }
    }

    // R(Read)
    public Page<ReviewResponse> page(String keyword, int page, String option) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> reviews = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색어가 있는 경우
            switch (option){
                case "1":
                    reviews = reviewRepository.findByTitleContaining(keyword, pageable);
                    break;
                case "2":
                    reviews = reviewRepository.findByContentContaining(keyword, pageable);
                    break;
            }

        } else {
            // 검색어가 없는 경우
            reviews = reviewRepository.findAll(pageable);
        }

        return reviews
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .nickname(review.getUser().getNickname())
                        .title(review.getTitle())
                        .content(review.getContent())
                        .createDate(review.getCreateDate())
                        .build());
    }

    public ReviewResponse readDetail(Long boardNum) {
        Review review = reviewRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 댓글 목록 가져오기
        List<Comment> comments = review.getComments();
        // Comment 객체를 CommentResponse 로 변환
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> CommentResponse.builder()
                        .nickname(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createDate(comment.getCreateDate())
                        .build())
                .collect(Collectors.toList());

        return ReviewResponse.builder()
                .id(review.getId())
                .nickname(review.getUser().getNickname())
                .title(review.getTitle())
                .content(review.getContent())
                .createDate(review.getCreateDate())
                .commentResponses(commentResponses)
                .build();
    }

    // U(Update)
    public ReviewResponse editP(Long boardNum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            Review review = reviewRepository.findById(boardNum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

            if(review.getUser().getId().equals(customUserDetails.getId())){
                return ReviewResponse.builder()
                        .id(review.getId())
                        .nickname(review.getUser().getNickname())
                        .title(review.getTitle())
                        .content(review.getContent())
                        .createDate(review.getCreateDate())
                        .build();
            }else{
                throw new IllegalArgumentException("본인이 작성한 글만 수정 가능합니다.");
            }
        }else {
            throw new IllegalArgumentException("로그인 후 이용하세요.");
        }


    }
}
