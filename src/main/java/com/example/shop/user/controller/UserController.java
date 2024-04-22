package com.example.shop.user.controller;

import com.example.shop.user.dto.requests.JoinRequest;
import com.example.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginP(){
        return "user/login";
    }

    @GetMapping("/join")
    public String joinP(){
        return "user/join";
    }

    @PostMapping("/joinProc")
    public String joinProc(@Valid JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            // 에러 메시지를 모델에 추가
            bindingResult.getFieldErrors()
                    .forEach(errors -> {
                        model.addAttribute(errors.getField() + "Error");
                    });
            return "user/join";
        }

        userService.join(joinRequest);
        return "redirect:/login";
    }
}
