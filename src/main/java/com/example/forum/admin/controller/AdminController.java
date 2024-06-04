package com.example.forum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @GetMapping("/admin")
    public String adminP(){
        return "admin/index";
    }

    @GetMapping("/admin/userManagement")
    public String userManagement(){
        return "admin/userManagement";
    }

    @GetMapping("/admin/users/{userId}")
    public String getUserDetail(@PathVariable("userId") Long userId, Model model){
        model.addAttribute("userId", userId);
        return "admin/detail";
    }
}
