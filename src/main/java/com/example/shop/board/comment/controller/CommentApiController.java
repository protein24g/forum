package com.example.shop.board.comment.controller;

import com.example.shop.board.comment.dto.requests.CommentRequest;
import com.example.shop.board.comment.dto.response.CommentResponse;
import com.example.shop.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentApiController {
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
    public ResponseEntity<?> deleteComment(@PathVariable("commentNum") Long commentNum){
        try{
            String url = commentService.deleteComment(commentNum);
            return ResponseEntity.status(HttpStatus.OK).body(url);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
