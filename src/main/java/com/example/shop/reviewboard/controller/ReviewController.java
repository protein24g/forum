package com.example.shop.reviewboard.controller;

import com.example.shop.qnaboard.dto.response.QnaResponse;
import com.example.shop.reviewboard.dto.requests.ReviewRequest;
import com.example.shop.reviewboard.dto.response.ReviewResponse;
import com.example.shop.reviewboard.service.ReviewService;
import com.example.shop.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
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
    public String reviewP(Model model,
                      @RequestParam(value = "keyword", required = false) String keyword,
                      @RequestParam(value = "page", defaultValue = "0") int page,
                      @RequestParam(value = "option", required = false) String option) {
        Page<ReviewResponse> reviewResponses;

        if (keyword != null && !keyword.isEmpty()) { // 키워드가 있으면
            reviewResponses = reviewService.page(keyword, page, option); // 검색 페이징
        } else {
            reviewResponses = reviewService.page("", page, ""); // 기본 리스트 페이징
        }

        int currentPage = reviewResponses.getNumber(); // 현재 페이지 번호
        int totalPages = reviewResponses.getTotalPages();
        model.addAttribute("startPage", Math.max(0, (currentPage - 5)));
        model.addAttribute("endPage", Math.min(totalPages - 1, (currentPage + 5)));
        model.addAttribute("boards", reviewResponses);
        model.addAttribute("keyword", keyword); // 검색어를 모델에 추가하여 뷰에서 사용할 수 있도록 함
        return "/reviewboard/list";
    }

    @GetMapping("/review/{boardNum}")
    public String reviewDetailP(@PathVariable("boardNum") Long boardNum, Model model, Authentication authentication){
        try {
            ReviewResponse reviewResponse = reviewService.readDetail(boardNum);
            model.addAttribute("review", reviewResponse);
            model.addAttribute("comments", reviewResponse.getCommentResponses());

            if(authentication != null){
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                if(customUserDetails.getUsername().equals(reviewResponse.getNickname())){
                    model.addAttribute("isUser", "edit");
                }
            }
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna");
            return "message/main";
        }
        return "reviewboard/detail";
    }

    // U(Update)
    @GetMapping("/review/edit/{boardNum}")
    public String editP(@PathVariable("boardNum") Long boardNum, Model model){
        try{
            ReviewResponse reviewResponse = reviewService.editP(boardNum);
            model.addAttribute("review", reviewResponse);
        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/review/" + boardNum);
        }
        return "reviewboard/edit";
    }

    // D(Delete)
}
