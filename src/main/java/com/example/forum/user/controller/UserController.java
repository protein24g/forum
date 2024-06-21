package com.example.forum.user.controller;

import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.user.dto.requests.CustomUserDetails;
import com.example.forum.user.dto.requests.JoinRequest;
import com.example.forum.user.service.UserAuthService;
import com.example.forum.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 유저 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserAuthService userAuthService;
    private final AuthenticationService authenticationService;

    /**
     * 로그인 페이지
     *
     * @param model Model 객체
     * @param error 에러 메시지
     * @return 로그인 페이지 뷰
     */
    @GetMapping("/login")
    public String login(Model model, @RequestParam(name = "error", defaultValue = "none") String error){
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
     * 마이페이지
     *
     * @return 마이페이지 뷰
     */
    @GetMapping("/mypage")
    public String myPage(){ return "user/mypage/index"; }

    /**
     * 마이페이지 게시글 조회
     *
     * @param model Model 객체
     * @param id 탭 ID
     * @return 마이페이지 게시글 뷰
     */
    @GetMapping("/mypage/boards")
    public String myPageBoards(Model model, @RequestParam(name = "id") String id){

        if(id.equals("myBoards")){
            model.addAttribute("id", "myBoards");
            model.addAttribute("title", "내가 쓴 글");
        } else if (id.equals("myCommentBoards")) {
            model.addAttribute("id", "myCommentBoards");
            model.addAttribute("title", "댓글 단 글");
        }
        return "user/mypage/boards";
    }

    /**
     * 특정 사용자 상세 페이지
     *
     * @return
     */
    @GetMapping("/userinfo/{nickname}")
    public String userinfoPage(Model model, @PathVariable("nickname") String nickname){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            if (customUserDetails.getUsername().equals(nickname)){
                return "redirect:/mypage";
            }
        }
        if(userAuthService.existsByNickname(nickname)){
            model.addAttribute("nickname", nickname);
            return "user/userinfo/index";
        } else{
            model.addAttribute("msg", "존재하지 않는 유저입니다");
            model.addAttribute("url", "/");
            return "message/index";
        }
    }

    /**
     * 특정 사용자 게시글 조회
     *
     * @param model Model 객체
     * @param id 탭 ID
     * @return 마이페이지 게시글 뷰
     */
    @GetMapping("/userinfo/{nickname}/boards")
    public String myPageBoards(@PathVariable(name = "nickname") String nickname,
                               Model model, @RequestParam(name = "id") String id){

        if(id.equals("myBoards")){
            model.addAttribute("nickname", nickname);
            model.addAttribute("id", "myBoards");
            model.addAttribute("title", "작성 글");
        } else if (id.equals("myCommentBoards")) {
            model.addAttribute("nickname", nickname);
            model.addAttribute("id", "myCommentBoards");
            model.addAttribute("title", "댓글 단 글");
        }
        return "user/userinfo/boards";
    }
}
