package com.example.shop.board.comment.controller;

import com.example.shop.board.comment.service.CommentService;
import com.example.shop.board.comment.dto.requests.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // C(Create)
    @PostMapping("/review/{boardNum}/comments")
    public String createReview(@PathVariable("boardNum") Long boardNum, Model model, CommentRequest dto) {
        try {
            commentService.createReview(boardNum, dto);
            model.addAttribute("msg", "댓글 작성완료");
            model.addAttribute("url", "/review/" + boardNum);

        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/review/" + boardNum);
        }
        return "redirect:/review/" + boardNum;
    }

    @PostMapping("/qna/{boardNum}/comments")
    public String createQna(@PathVariable("boardNum") Long boardNum, Model model, CommentRequest dto) {
        try {
            commentService.createQna(boardNum, dto);
            model.addAttribute("msg", "댓글 작성완료");
            model.addAttribute("url", "/qna/" + boardNum);

        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna/" + boardNum);
        }
        return "redirect:/qna/" + boardNum;
    }

    // U(Update)
    @PostMapping("/comment/edit/{commentNum}")
    public String updateComment(@PathVariable("commentNum") Long commentNum, Model model, CommentRequest dto){
        try{
            String url = commentService.updateComment(commentNum, dto);
            model.addAttribute("msg", "댓글 수정완료");
            model.addAttribute("url", url);
        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/");
        }
        return "message/main";
    }

    // D(Delete)
    @PostMapping("/comment/delete/{commentNum}")
    public String deleteComment(@PathVariable("commentNum") Long commentNum, Model model){
        try{
            String url = commentService.deleteComment(commentNum);
            model.addAttribute("msg", "댓글 삭제완료");
            model.addAttribute("url", url);
        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/");
        }
        return "message/main";
    }
}
