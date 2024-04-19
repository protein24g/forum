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
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
    public String create(ReviewRequest dto){
        reviewService.create(dto);
        return "reviewboard/review";
    }

    // R(Read)
    @GetMapping("/review")
    public String reviewP(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            // getPrincipal()이 CustomUserDetails 인스턴스인 경우
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("nickname", customUserDetails.getUsername());
        }
        model.addAttribute("boards", reviewService.readAll());
        return "reviewboard/review";
    }

}
