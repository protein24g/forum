package com.example.shop.board.reviewboard.controller;

import com.example.shop.board.reviewboard.dto.response.ReviewResponse;
import com.example.shop.board.reviewboard.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {
    private final ReviewService reviewService;

    // R(Read)
    @GetMapping("/api/review")
    public ResponseEntity<?> reviewP(
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "option", required = false) String option) {
        Page<ReviewResponse> reviewResponses;
        System.out.println("페이지 " + page + " 요청됨");
        if (keyword != null) { // 키워드가 있으면
            reviewResponses = reviewService.page(keyword, page, option); // 검색 페이징
        } else {
            reviewResponses = reviewService.page("", page, ""); // 기본 리스트 페이징
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponses);
    }

    @GetMapping("/api/review/{boardNum}")
    public ResponseEntity<?> loadReviewDetails(@PathVariable("boardNum") Long boardNum){
        try {
            ReviewResponse reviewResponse = reviewService.readDetail(boardNum);
            return ResponseEntity.status(HttpStatus.OK).body(reviewResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}