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
    public ResponseEntity<?> reviewP(Model model,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "option", required = false) String option) {
        Page<ReviewResponse> reviewResponses;
        System.out.println(keyword + Integer.toString(page) + option);
        if (keyword != null && option != null) { // 키워드가 있으면
            reviewResponses = reviewService.page(keyword, page, option); // 검색 페이징
        } else {
            reviewResponses = reviewService.page("", page, ""); // 기본 리스트 페이징
        }
//
//        int currentPage = reviewResponses.getNumber(); // 현재 페이지 번호
//        int totalPages = reviewResponses.getTotalPages();
//        model.addAttribute("startPage", Math.max(0, (currentPage - 5)));
//        model.addAttribute("endPage", Math.min(totalPages - 1, (currentPage + 5)));
//        model.addAttribute("boards", reviewResponses);
//        model.addAttribute("keyword", keyword); // 검색어를 모델에 추가하여 뷰에서 사용할 수 있도록 함
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponses);
    }

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
