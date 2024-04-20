package com.example.shop.reviewboard.service;

import com.example.shop.reviewboard.dto.requests.ReviewRequest;
import com.example.shop.reviewboard.dto.response.ReviewResponse;
import com.example.shop.reviewboard.entity.Review;
import com.example.shop.reviewboard.repository.ReviewRepository;
import com.example.shop.user.dto.CustomUserDetails;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
import com.sun.nio.sctp.IllegalReceiveException;
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
import java.util.stream.Collector;
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
                    .reviewScore(5.0)
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
                    .reviewScore(review.getReviewScore())
                    .build();
        }else{
            throw new IllegalArgumentException("로그인 후 이용 가능합니다.");
        }
    }

    // R(Read)
    public List<ReviewResponse> page(int page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("id")));
        return reviewRepository.findAll(pageable).stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .nickname(review.getUser().getNickname())
                        .title(review.getTitle())
                        .content(review.getContent())
                        .createDate(review.getCreateDate())
                        .reviewScore(review.getReviewScore())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> readAll() {
        return reviewRepository.findAll().stream()
                .map(review -> ReviewResponse.builder()
                            .id(review.getId())
                            .nickname(review.getUser().getNickname())
                            .title(review.getTitle())
                            .content(review.getContent())
                            .createDate(review.getCreateDate())
                            .reviewScore(review.getReviewScore())
                            .build())
                .collect(Collectors.toList());
    }

    public ReviewResponse readDetail(Long boardNum, Long userId) {
        Review review = reviewRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        if(review.getUser().getId().equals(userId)){
            return ReviewResponse.builder()
                    .id(review.getId())
                    .nickname(review.getUser().getNickname())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .createDate(review.getCreateDate())
                    .reviewScore(review.getReviewScore())
                    .build();
        }else {
            throw new IllegalArgumentException("본인이 작성한 글만 읽기 가능합니다.");
        }
    }
}
