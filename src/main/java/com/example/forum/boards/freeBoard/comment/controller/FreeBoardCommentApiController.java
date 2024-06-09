package com.example.forum.boards.freeBoard.comment.controller;

import com.example.forum.boards.freeBoard.comment.dto.requests.FreeBoardCommentRequest;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
import com.example.forum.boards.freeBoard.comment.dto.response.FreeBoardCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class FreeBoardCommentApiController {
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;

    // C(Create)
    @PostMapping("/api/c/freeBoard/{boardId}/comments")
    public ResponseEntity<?> createCommentForBoard(@PathVariable("boardId") Long boardId, @RequestBody FreeBoardCommentRequest dto){
        try{
            freeBoardCommentServiceImpl.createCommentForBoard(boardId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 생성완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // R(Read)
    @GetMapping("/api/freeBoard/{boardId}/comments")
    public Page<FreeBoardCommentResponse> getCommentsForBoard(@PathVariable("boardId") Long boardId,
                                                              @RequestParam(name = "page", defaultValue = "0") int page){
        Page<FreeBoardCommentResponse> commentResponses = freeBoardCommentServiceImpl.getCommentsForBoard(boardId, page);
        return commentResponses;
    }

    // U(Update)
    @PutMapping("/api/freeBoardComments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody FreeBoardCommentRequest dto){
        try{
            freeBoardCommentServiceImpl.updateComment(commentId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 수정완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // D(Delete)
    @DeleteMapping("/api/freeBoardComments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId){
        try{
            freeBoardCommentServiceImpl.deleteComment(commentId);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
