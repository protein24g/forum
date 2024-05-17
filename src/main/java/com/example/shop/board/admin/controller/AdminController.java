package com.example.shop.board.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AdminController {
    @GetMapping("/admin")
    public String adminP(){
        return "admin/index";
    }

    @GetMapping("/admin/users/{userId}")
    public String getUserDetail(@PathVariable("userId") Long userId, Model model){
        model.addAttribute("userId", userId);
        return "admin/detail";
    }
}
