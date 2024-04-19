package com.example.shop.reviewboard.service;

import com.example.shop.reviewboard.dto.requests.ReviewRequest;
import com.example.shop.reviewboard.dto.response.ReviewResponse;
import com.example.shop.reviewboard.entity.Review;
import com.example.shop.reviewboard.repository.ReviewRepository;
import com.example.shop.user.dto.CustomUserDetails;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    // C(Create)
    public void create(ReviewRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

            Review board = reviewRepository.save(Review.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .reviewScore(5.0)
                    .user(user)
                    .build());

            user.getReviews().add(board);
            System.out.println("리뷰 작성 완료");
        }else{
            throw new IllegalStateException("로그인 후 이용 가능합니다.");
        }
    }

    public List<ReviewResponse> readAll() {
        return reviewRepository.findAll().stream()
                .map(review -> ReviewResponse.builder()
                            .nickname(review.getUser().getNickname())
                            .title(review.getTitle())
                            .content(review.getContent())
                            .createDate(review.getCreateDate())
                            .reviewScore(review.getReviewScore())
                            .build())
                .collect(Collectors.toList());
    }
}
