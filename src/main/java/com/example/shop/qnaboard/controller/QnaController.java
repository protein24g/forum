package com.example.shop.qnaboard.controller;

import com.example.shop.qnaboard.dto.requests.QnaRequest;
import com.example.shop.qnaboard.dto.response.QnaResponse;
import com.example.shop.qnaboard.service.QnaService;
import com.example.shop.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class QnaController {
    private final QnaService qnaService;

    // C(Create)
    @GetMapping("/qna/create")
    public String createP(){
        return "qnaboard/create";
    }

    @PostMapping("/qna/create")
    public String create(QnaRequest dto, Model model){
        try{
            QnaResponse qnaResponse = qnaService.create(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/qna/" + qnaResponse.getId());
            return "message/main";
        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/login");
            return "message/main";
        }
    }

    // R(Read)
    @GetMapping("/qna")
    public String qna(Model model, @RequestParam(value="page", defaultValue = "0") int page){
        Page<QnaResponse> qnaRespons = qnaService.page(page);
        model.addAttribute("currentPage", "board");
        model.addAttribute("boards", qnaRespons);
        return "/qnaboard/list";
    }

    @GetMapping("/qna/{boardNum}")
    public String qnaDetailP(@PathVariable("boardNum") Long boardNum, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            try{
                QnaResponse qnaResponse = qnaService.readDetail(boardNum, customUserDetails.getId());
                model.addAttribute("nickname", customUserDetails.getUsername());
                model.addAttribute("qna", qnaResponse);
                return "qnaboard/detail";
            } catch (IllegalArgumentException e){
                model.addAttribute("msg", e.getMessage()); // 게시글이 없거나, 다른 사람이 작성한 글
                model.addAttribute("url", "/qna");
                return "message/main";
            }
        }else{
            model.addAttribute("msg", "로그인 후 이용하세요.");
            model.addAttribute("url", "/login");
            return "message/main";
        }
    }
}
