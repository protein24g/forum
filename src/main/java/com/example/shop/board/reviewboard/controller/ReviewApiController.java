package com.example.shop.board.reviewboard.controller;

import com.example.shop.board.reviewboard.dto.requests.ReviewRequest;
import com.example.shop.board.reviewboard.dto.response.ReviewResponse;
import com.example.shop.board.reviewboard.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {
    private final ReviewService reviewService;

    // R(Read)
    @GetMapping("/api/reviews")
    public ResponseEntity<?> reviewP(
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "option", required = false) String option) {
        Page<ReviewResponse> reviewResponses;
        if (keyword != null) { // 키워드가 있으면
            reviewResponses = reviewService.page(keyword, page, option); // 검색 페이징
        } else {
            reviewResponses = reviewService.page("", page, ""); // 기본 리스트 페이징
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponses);
    }

    @GetMapping("/api/reviews/{reviewId}")
    public ResponseEntity<?> getReviewDetail(@PathVariable("reviewId") Long reviewId){
        try {
            ReviewResponse reviewResponse = reviewService.readDetail(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(reviewResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // U(Update)
    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewRequest dto){
        try{
            reviewService.edit(reviewId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("리뷰 수정완료");
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
