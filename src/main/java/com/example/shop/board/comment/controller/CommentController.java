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
    @PostMapping("/review/{reviewId}/comments")
    @ResponseBody
    public ResponseEntity<?> createReview(@PathVariable("reviewId") Long reviewId, @RequestBody CommentRequest dto){
        try{
            CommentResponse commentResponse = commentService.createReview(reviewId, dto);
            return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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

    // R(Read)
    @GetMapping("/review/{reviewId}/comments")
    @ResponseBody
    public Page<CommentResponse> findAllComment(@PathVariable("reviewId") Long reviewId,
                                                @RequestParam(name = "page", defaultValue = "0") int page){
        Page<CommentResponse> commentResponses = commentService.findAllComment(reviewId, page);
        return commentResponses;
    }

    // U(Update)
    @PostMapping("/comment/edit/{commentNum}")
    @ResponseBody
    public ResponseEntity<?> updateComment(@PathVariable("commentNum") Long commentNum, @RequestBody CommentRequest dto){
        try{
            commentService.updateComment(commentNum, dto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // D(Delete)
    @PostMapping("/comment/delete/{commentNum}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable("commentNum") Long commentNum){
        try{
            String url = commentService.deleteComment(commentNum);
            return ResponseEntity.status(HttpStatus.OK).body(url);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
