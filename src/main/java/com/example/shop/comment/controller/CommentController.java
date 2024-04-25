package com.example.shop.comment.controller;

import com.example.shop.comment.dto.requests.CommentRequest;
import com.example.shop.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/qna/{boardNum}/comments")
    public String createQna(@PathVariable("boardNum") Long boardNum, Model model, CommentRequest commentRequest){
        try {
            commentService.create(boardNum, commentRequest);
            model.addAttribute("msg", "댓글 작성완료.");
            model.addAttribute("url", "/qna/" + boardNum);

        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna/" + boardNum);
        }
        return "redirect:/qna/" + boardNum;
    }

    @PostMapping("/review/{boardNum}/comments")
    public String createRivew(@PathVariable("boardNum") Long boardNum, Model model, CommentRequest commentRequest){
        try {
            commentService.create(boardNum, commentRequest);
            model.addAttribute("msg", "댓글 작성완료.");
            model.addAttribute("url", "/review/" + boardNum);

        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/review/" + boardNum);
        }
        return "redirect:/qna/" + boardNum;
    }
}
