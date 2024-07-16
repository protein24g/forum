package com.example.forum.user.auth.controller;

import com.example.forum.user.auth.dto.requests.JoinRequest;
import com.example.forum.user.auth.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 유저 Auth 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class UserAuthController {
    private final UserAuthService userAuthService;

    /**
     * 로그인 페이지
     *
     * @param model Model 객체
     * @param error 에러 메시지
     * @return 로그인 페이지 뷰
     */
    @GetMapping("/login")
    public String login(Model model, @RequestParam(name = "error", defaultValue = "false") String error){
        if(error.equals("true")){
            model.addAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다");
            model.addAttribute("url", "/login");
            return "message/index";
        }
        return "user/login";
    }

    /**
     * 회원가입 페이지
     *
     * @return 회원가입 페이지 뷰
     */
    @GetMapping("/join")
    public String joinP(){
        return "user/join";
    }

    /**
     * 회원가입 처리
     * @param joinRequest 회원가입 정보
     * @param result 유효성 검사 결과
     * @param model Model 객체
     * @return 회원가입 결과 메시지 페이지 뷰
     */
    @PostMapping("/joinProc")
    public String joinProc(@Valid JoinRequest joinRequest, // 클라이언트로부터 받은 JoinRequest 객체에 대해 @Valid 애너테이션을 통해 유효성 검증을 진행합니다.
                           BindingResult result, // 유효성 검사 과정에서 발생한 오류들을 담는 BindingResult 객체. 유효성 검사를 통과하지 못한 필드와 관련된 구체적인 오류 정보들을 가지고 있습니다.
                           Model model) { // 뷰로 데이터를 전달하기 위한 Model 객체
        // 검증 과정에서 오류가 발견되었는지 여부를 확인합니다.
        if (result.hasErrors()) {
            model.addAttribute("msg", result.hasErrors());
            model.addAttribute("url", "/join");
            return "message/index";
        }

        // 유효성 검증을 통과했을 경우의 로직을 작성합니다.
        // try-catch 블록 내에서 userService의 join 메소드를 호출하여 실제 회원 가입 처리를 시도합니다.
        try {
            userAuthService.join(joinRequest);
            // 회원 가입 성공 시, 성공 메시지와 함께 메인 페이지("/")로 리다이렉션 정보를 model에 추가합니다.
            model.addAttribute("msg", "회원가입 성공");
            model.addAttribute("url", "/");
            // 회원 가입 과정 중 IllegalArgumentException이 발생하면,
            // 예외 메시지와 함께 회원 가입 페이지로 다시 리다이렉션 정보를 model에 추가합니다.
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/join");
        }

        // 모든 처리가 끝난 후, message/index 뷰로 이동하여 메시지를 보여줍니다.
        // 여기서는 회원 가입 성공, 실패 메시지와 같은 중요 정보들을 사용자에게 전달합니다.
        return "message/index";
    }

    /**
     * 로그인 아이디 존재 여부
     * @param loginId 로그인 ID
     * @return 존재 여부
     */
    @GetMapping("/checkLoginId")
    public ResponseEntity<?> checkLoginId(@RequestParam(name = "loginId") String loginId) {
        System.out.println("들어왔다 아이디");
        System.out.println(loginId);
        boolean res = userAuthService.existsByLoginId(loginId);
        System.out.println("결과 : " + res);
        if(res){
            return ResponseEntity.status(HttpStatus.OK).body("false");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("true");
        }
    }

    /**
     * 닉네임 존재 여부
     * @param nickname 닉네임
     * @return 존재 여부
     */
    @GetMapping("/checkNickname")
    public ResponseEntity<?> checkNickname(@RequestParam(name = "nickname") String nickname) {
        boolean res = userAuthService.existsByNickname(nickname);
        if(res){
            return ResponseEntity.status(HttpStatus.OK).body("false");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("true");
        }
    }
}
