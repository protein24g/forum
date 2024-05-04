package com.example.shop.board.reviewboard.controller;

import com.example.shop.board.reviewboard.dto.response.ReviewResponse;
import com.example.shop.board.reviewboard.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {
    private ReviewService reviewService;

    @GetMapping("/api/review/{boardNum}")
    public ResponseEntity<?> loadReviewDetails(@PathVariable("boardNum") Long boardNum){
        try {
            ReviewResponse reviewResponse = reviewService.readDetail(boardNum);
            return ResponseEntity.status(HttpStatus.OK).body(reviewResponse);

//            int currentPage = reviewResponse.getCommentResponses().getNumber(); // 현재 페이지 번호
//            int totalPages = reviewResponse.getCommentResponses().getTotalPages();
//
//            model.addAttribute("startPage", Math.max(0, (currentPage - 5)));
//            model.addAttribute("endPage", Math.min(totalPages - 1, (currentPage + 5)));
//            model.addAttribute("comments", reviewResponse.getCommentResponses());
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
