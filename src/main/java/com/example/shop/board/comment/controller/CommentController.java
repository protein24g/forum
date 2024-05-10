package com.example.shop.board.comment.controller;

import com.example.shop.board.comment.service.CommentService;
import com.example.shop.board.comment.dto.requests.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    // C(Create)
    @PostMapping("/qna/{qnaId}/comments")
    public String createQna(@PathVariable("qnaId") Long qnaId, Model model, CommentRequest dto) {
        try {
            commentService.createCommentForQna(qnaId, dto);
            model.addAttribute("msg", "댓글 작성완료");
            model.addAttribute("url", "/qna/" + qnaId);

        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna/" + qnaId);
        }
        return "redirect:/qna/" + qnaId;
    }


}
