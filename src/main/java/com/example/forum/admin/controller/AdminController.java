package com.example.forum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 관리자 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    /**
     * 관리자 페이지로 이동
     *
     * @return 관리자 페이지 URL
     */
    @GetMapping("/admin")
    public String adminP(){
        return "admin/index";
    }

    /**
     * 사용자 관리 페이지로 이동
     *
     * @return 사용자 관리 페이지 URL
     */
    @GetMapping("/admin/userManagement")
    public String userManagement(){
        return "admin/userManagement";
    }

    /**
     * 블랙리스트 관리 페이지로 이동
     *
     * @return 블랙리스트 관리 페이지 URL
     */
    @GetMapping("/admin/blacklist")
    public String blacklist(){
        return "admin/blacklist";
    }

    /**
     * 특정 사용자의 상세 정보 페이지로 이동
     *
     * @param userId 사용자 ID
     * @param model  Model 객체
     * @return 사용자 상세 정보 페이지 URL
     */
    @GetMapping("/admin/users/{userId}")
    public String getUserDetail(@PathVariable("userId") Long userId, Model model){
        model.addAttribute("userId", userId);
        return "admin/detail";
    }
}
