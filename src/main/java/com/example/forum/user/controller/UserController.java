package com.example.forum.user.controller;

import com.example.forum.boards.freeboard.board.dto.response.FreeBoardResponse;
import com.example.forum.user.dto.requests.JoinRequest;
import com.example.forum.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/join")
    public String joinP(){
        return "user/join";
    }

    @PostMapping("/joinProc")
    public String joinProc( @Valid JoinRequest joinRequest, // 클라이언트로부터 받은 JoinRequest 객체에 대해 @Valid 애너테이션을 통해 유효성 검증을 진행합니다.
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

        // 모든 처리가 끝난 후, message/index 뷰로 이동하여 메시지를 보여줍니다.
        // 여기서는 회원 가입 성공, 실패 메시지와 같은 중요 정보들을 사용자에게 전달합니다.
        return "message/index";
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

    // R(Read)
    @GetMapping("/mypage")
    public String myPage(){ return "user/mypage/index"; }

    @GetMapping("/mypage/boards")
    public String myPageBoards(Model model, @RequestParam(name = "id") String id,
                               @RequestParam(name = "page", defaultValue = "0") int page){

        Page<FreeBoardResponse> boardResponses = userService.myPageBoards(id, page);
        int startPage = (int) (Math.floor((double) page / 5) * 5) + 1;
        int endPage = Math.min(startPage + 4, boardResponses.getTotalPages());

        if(id.equals("myboards")){
            model.addAttribute("id", "myboards");
            model.addAttribute("title", "내가 쓴 글");
        } else if (id.equals("mycommentboards")) {
            model.addAttribute("id", "mycommentboards");
            model.addAttribute("title", "댓글 단 글");
        }
        model.addAttribute("boards", boardResponses);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "user/mypage/boards";
    }
}
