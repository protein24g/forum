package com.example.shop.user.controller;

import com.example.shop.user.dto.requests.JoinRequest;
import com.example.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String joinProc( @Valid JoinRequest joinRequest, // 클라이언트로부터 받은 JoinRequest 객체에 대해 @Valid 애너테이션을 통해 유효성 검증을 진행합니다.
                            BindingResult result, // 유효성 검사 과정에서 발생한 오류들을 담는 BindingResult 객체. 유효성 검사를 통과하지 못한 필드와 관련된 구체적인 오류 정보들을 가지고 있습니다.
                            Model model) { // 뷰로 데이터를 전달하기 위한 Model 객체.
        // 검증 과정에서 오류가 발견되었는지 여부를 확인합니다.
        if (result.hasErrors()) {
            // 검증 실패 시, 에러 메시지들을 뷰에 전달합니다.
            // getAllErrors 메소드를 통해 발생한 모든 오류들을 가져와 model에 추가합니다.
            model.addAttribute("errors", result.getAllErrors());
            // 회원 가입 페이지(user/join)로 리다이렉트합니다. 이때, 오류 메시지들이 포함된 상태로 뷰를 렌더링하여 사용자가 오류를 인지하고 수정할 수 있도록 합니다.
            return "user/join";
        }

        // 유효성 검증을 통과했을 경우의 로직을 작성합니다.
        // try-catch 블록 내에서 userService의 join 메소드를 호출하여 실제 회원 가입 처리를 시도합니다.
        try {
            userService.join(joinRequest);
            // 회원 가입 성공 시, 성공 메시지와 함께 메인 페이지("/")로 리다이렉션 정보를 model에 추가합니다.
            model.addAttribute("msg", "회원가입 성공");
            model.addAttribute("url", "/");
            // 회원 가입 과정 중 IllegalArgumentException이 발생하면,
            // 예외 메시지와 함께 회원 가입 페이지로 다시 리다이렉션 정보를 model에 추가합니다.
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/join");
        }

        // 모든 처리가 끝난 후, message/main 뷰로 이동하여 메시지를 보여줍니다.
        // 여기서는 회원 가입 성공, 실패 메시지와 같은 중요 정보들을 사용자에게 전달합니다.
        return "message/main";
    }

    @GetMapping("/checkLoginId")
    public ResponseEntity<Boolean> checkLoginId(@RequestParam(name = "loginId") String loginId) {
        boolean isAvailable = userService.isLoginIdAvailable(loginId);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam(name = "nickname") String nickname) {
        boolean isAvailable = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(isAvailable);
    }
}
