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
    @GetMapping("/review/create")
    public String createP(){
        return "reviewboard/create";
    }

    @PostMapping("/review/create")
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
    @GetMapping("/review")
    public String reviewP() {
        return "/reviewboard/list";
    }

    @GetMapping("/review/{boardNum}")
    public String reviewDetailP(@PathVariable("boardNum") Long boardNum, Model model){
        try {
            String writer = reviewService.getWriter(boardNum);
            model.addAttribute("writer", writer);
            model.addAttribute("reviewId", boardNum);
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna");
            return "message/main";
        }
        return "reviewboard/detail";
    }

    @PostMapping("/review/edit/{boardNum}")
    public String edit(@PathVariable("boardNum") Long boardNum, Model model, ReviewRequest dto){
        try{
            ReviewResponse reviewResponse = reviewService.edit(boardNum, dto);
            model.addAttribute("msg", "게시글 수정 완료.");
            model.addAttribute("url", "/review/" + boardNum);
        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/review/" + boardNum);
        }
        return "message/main";
    }

    // D(Delete)
}
