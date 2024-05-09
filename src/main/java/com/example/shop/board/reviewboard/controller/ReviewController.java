package com.example.shop.board.reviewboard.controller;

import com.example.shop.board.reviewboard.dto.requests.ReviewRequest;
import com.example.shop.board.reviewboard.dto.response.ReviewResponse;
import com.example.shop.board.reviewboard.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Transactional
public class ReviewController {
    private final ReviewService reviewService;

    // C(Create)
    @GetMapping("/reviews/create")
    public String createP(){
        return "reviewboard/create";
    }

    @PostMapping("/reviews/create")
    public String create(ReviewRequest dto, Model model){
        try {
            ReviewResponse reviewResponse = reviewService.create(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/review/" + reviewResponse.getId());
            return "message/main";

        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/login");
            return "message/main";
        }
    }

    // R(Read)
    @GetMapping("/reviews")
    public String listReviews() {
        return "/reviewboard/list";
    }

    @GetMapping("/reviews/{reviewId}")
    public String showReviewDetail(@PathVariable("reviewId") Long reviewId, Model model){
        try {
            String writer = reviewService.getWriter(reviewId);
            model.addAttribute("writer", writer);
            model.addAttribute("reviewId", reviewId);
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna");
            return "message/main";
        }
        return "reviewboard/detail";
    }
}
