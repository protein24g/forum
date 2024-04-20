package com.example.shop.reviewboard.controller;

import com.example.shop.reviewboard.dto.requests.ReviewRequest;
import com.example.shop.reviewboard.dto.response.ReviewResponse;
import com.example.shop.reviewboard.service.ReviewService;
import com.example.shop.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        try{
            ReviewResponse reviewResponse = reviewService.create(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/review" + reviewResponse.getId());
            return "message/main";
        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/login");
            return "message/main";
        }
    }

    // R(Read)
    @GetMapping("/review")
    public String reviewP(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            // getPrincipal()이 CustomUserDetails 인스턴스인 경우
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("nickname", customUserDetails.getUsername());
            model.addAttribute("boards", reviewService.readAll());
            return "reviewboard/review";
        }else{
            model.addAttribute("msg", "로그인 후 이용하세요.");
            model.addAttribute("url", "/login");
            return "message/main";
        }
    }

    @GetMapping("/review/{boardNum}")
    public String reviewDetailP(@PathVariable("boardNum") Long boardNum, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            try{
                ReviewResponse reviewResponse = reviewService.readDetail(boardNum, customUserDetails.getId());
                model.addAttribute("nickname", customUserDetails.getUsername());
                model.addAttribute("review", reviewResponse);
                return "reviewboard/detail";
            } catch (IllegalArgumentException e){
                model.addAttribute("msg", e.getMessage());
                model.addAttribute("url", "/review");
                return "message/main";
            }
        }else{
            model.addAttribute("msg", "로그인 후 이용하세요.");
            model.addAttribute("url", "/login");
            return "message/main";
        }
    }
}
