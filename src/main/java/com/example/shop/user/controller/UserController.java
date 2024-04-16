package com.example.shop.user.controller;

import com.example.shop.user.dto.requests.JoinDTO;
import com.example.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String joinProc(@Valid JoinDTO joinDTO, BindingResult bindingResult) {
        //  @Valid를 사용하여 JoinDTO 객체의 검증
        // bindingResult.hasErrors() 로 오류가 발생했는지 확인 가능
        // redirectAttributes 로 오류 메시지와 함께 입력했던 데이터를 유지하기 위해 사용

        if (bindingResult.hasErrors()) {
            return "redirect:/join"; // 회원가입 페이지로 리다이렉트
        }

        // 모든 검증 로직을 통과한 후, 사용자 가입 로직 호출
        userService.join(joinDTO);
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }
}
