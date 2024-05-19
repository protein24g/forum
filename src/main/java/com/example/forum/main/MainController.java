package com.example.forum.main;

import com.example.forum.user.dto.requests.CustomUserDetails;
import com.example.forum.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/")
    public String mainP(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            // getPrincipal()이 CustomUserDetails 인스턴스인 경우
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("nickname", customUserDetails.getUsername());
        } else {
            // getPrincipal()이 CustomUserDetails 인스턴스가 아닌 경우 (예: "anonymousUser")
            // 여기에 대한 처리 로직
            model.addAttribute("nickname", "anonymousUser");
        }
        return "main/index";
    }
}
