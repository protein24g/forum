package com.example.shop.main;

import com.example.shop.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/")
    public String mainP(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            // getPrincipal()이 CustomUserDetails 인스턴스인 경우
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("nickname", customUserDetails.getNickname()); // 또는 getNickname()을 사용할 수 있습니다.
        } else {
            // getPrincipal()이 CustomUserDetails 인스턴스가 아닌 경우 (예: "anonymousUser")
            // 여기에 대한 처리 로직
            model.addAttribute("nickname", "Guest");
        }
        return "main";
    }
}
