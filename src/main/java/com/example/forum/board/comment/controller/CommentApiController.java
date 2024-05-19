package com.example.forum.board.comment.controller;

import com.example.forum.board.comment.dto.requests.CommentRequest;
import com.example.forum.board.comment.dto.response.CommentResponse;
import com.example.forum.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;

    // C(Create)
    @PostMapping("/api/c/boards/{boardId}/comments")
    public ResponseEntity<?> createCommentForBoard(@PathVariable("boardId") Long boardId, @RequestBody CommentRequest dto){
        try{
            commentService.createCommentForBoard(boardId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 생성완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // R(Read)
    @GetMapping("/api/boards/{boardId}/comments")
    public Page<CommentResponse> getCommentsForBoard(@PathVariable("boardId") Long boardId,
                                                @RequestParam(name = "page", defaultValue = "0") int page){
        Page<CommentResponse> commentResponses = commentService.getCommentsForBoard(boardId, page);
        return commentResponses;
    }

    // U(Update)
    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest dto){
        try{
            commentService.updateComment(commentId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 수정완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // D(Delete)
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId){
        try{
            commentService.deleteComment(commentId);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
