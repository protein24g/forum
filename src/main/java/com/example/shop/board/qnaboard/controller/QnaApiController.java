package com.example.shop.board.qnaboard.controller;

import com.example.shop.board.qnaboard.dto.response.QnaResponse;
import com.example.shop.board.qnaboard.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QnaApiController {
    private final QnaService qnaService;

    @GetMapping("/api/qna/{boardNum}")
    public String qnaDetailP(@PathVariable("boardNum") Long boardNum, Model model){
        try {
            QnaResponse qnaResponse = qnaService.readDetail(boardNum);
            model.addAttribute("qna", qnaResponse);
            model.addAttribute("comments", qnaResponse.getCommentResponses());
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna");
            return "message/main";
        }
        return "qnaboard/detail";
    }
}
