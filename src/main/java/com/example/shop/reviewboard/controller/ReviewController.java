package com.example.shop.reviewboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    // C(Create)
    @GetMapping("/review")
    public String reviewP(){
        return "/review/list";
    }
}
