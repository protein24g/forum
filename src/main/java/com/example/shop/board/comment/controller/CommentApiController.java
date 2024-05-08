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
    @PostMapping("/api/reviews/{reviewId}/comments")
    public ResponseEntity<?> createCommentForReview(@PathVariable("reviewId") Long reviewId, @RequestBody CommentRequest dto){
        try{
            CommentResponse commentResponse = commentService.createCommentForReview(reviewId, dto);
            return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // R(Read)
    @GetMapping("/reviews/{reviewId}/comments")
    public Page<CommentResponse> getCommentsForReview(@PathVariable("reviewId") Long reviewId,
                                                @RequestParam(name = "page", defaultValue = "0") int page){
        Page<CommentResponse> commentResponses = commentService.getAllCommentsForReview(reviewId, page);
        return commentResponses;
    }

    // U(Update)
    @PatchMapping("/api/comment/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest dto){
        try{
            commentService.updateComment(commentId, dto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // D(Delete)
    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId){
        try{
            String url = commentService.deleteComment(commentId);
            return ResponseEntity.status(HttpStatus.OK).body(url);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
