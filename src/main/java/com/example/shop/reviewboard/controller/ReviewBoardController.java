package com.example.shop.reviewboard.controller;

import com.example.shop.reviewboard.service.ReviewBoardService;
import com.example.shop.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ReviewBoardController {
    private final ReviewBoardService reviewBoardService;

    @GetMapping("/review")
    public String reviewP(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            // getPrincipal()이 CustomUserDetails 인스턴스인 경우
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("nickname", customUserDetails.getUsername());
        }
        return "board/review";
    }
}
