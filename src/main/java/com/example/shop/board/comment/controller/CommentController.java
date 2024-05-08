package com.example.shop.board.comment.controller;

import com.example.shop.board.comment.dto.response.CommentResponse;
import com.example.shop.board.comment.entity.Comment;
import com.example.shop.board.comment.service.CommentService;
import com.example.shop.board.comment.dto.requests.CommentRequest;
import com.example.shop.board.reviewboard.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    // C(Create)
    @PostMapping("/qna/{reviewId}/comments")
    public String createQna(@PathVariable("reviewId") Long reviewId, Model model, CommentRequest dto) {
        try {
            commentService.createCommentForQna(reviewId, dto);
            model.addAttribute("msg", "댓글 작성완료");
            model.addAttribute("url", "/qna/" + reviewId);

        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna/" + reviewId);
        }
        return "redirect:/qna/" + reviewId;
    }


}
