package com.example.forum.user.profile.controller;

import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.user.auth.dto.requests.CustomUserDetails;
import com.example.forum.user.auth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 유저 프로필 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class UserProfileController {
    private final AuthenticationService authenticationService;
    private final UserAuthService userAuthService;

    /**
     * 마이페이지
     *
     * @return 마이페이지 뷰
     */
    @GetMapping("/mypage")
    public String myPage(){ return "user/mypage/index"; }

    /**
     * 마이페이지 내가 쓴 방명록 조회
     *
     * @param model Model 객체
     * @return 마이페이지 내가 쓴 방명록 뷰
     */
    @GetMapping("/mypage/guestBoards")
    public String myPageGuestBoards(Model model){
        model.addAttribute("title", "내가 쓴 방명록");
        return "user/mypage/guestBoards";
    }

    /**
     * 마이페이지 게시글 조회
     *
     * @param model Model 객체
     * @param id 탭 ID
     * @return 마이페이지 내가 쓴 게시글 뷰
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
