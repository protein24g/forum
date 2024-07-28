package com.example.forum.base.comment.controller;

import com.example.forum.base.comment.dto.request.CommentRequest;
import com.example.forum.base.comment.dto.response.CommentResponse;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardLikeResponse;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardCommentApiController {
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;

    // C(Create)
    /**
     * 특정 게시글에 댓글 생성
     *
     * @param boardId 게시글의 고유 식별자
     * @param dto     생성할 댓글 정보가 담긴 DTO
     * @return        HTTP 응답(ResponseEntity) 객체
     */
    @PostMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<?> createCommentForBoard(@PathVariable("boardId") Long boardId, @RequestBody CommentRequest dto,
                                                   @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            try{
                freeBoardCommentServiceImpl.createCommentForBoard(boardId, dto);
                return ResponseEntity.status(HttpStatus.OK).body("댓글 생성완료");
            } catch (IllegalArgumentException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 특정 댓글 좋아요
     *
     * @param commentId
     * @return
     */
    @PostMapping("/api/boards/{commentId}/comments/like")
    public ResponseEntity<?> insertCommentLike(@PathVariable(name = "commentId") Long commentId,
                                               @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            try{
                freeBoardCommentServiceImpl.insertCommentLike(commentId);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (IllegalArgumentException e){
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        } else {
            return null;
        }
    }

    // R(Read)
    /**
     * 특정 게시글에 작성된 댓글 목록을 페이지 단위로 반환
     *
     * @param boardId 게시글의 고유 식별자
     * @param page    페이지 번호
     * @return        댓글 목록 페이지
     */
    @GetMapping("/api/boards/{boardId}/comments")
    public Page<CommentResponse> getCommentsForBoard(@PathVariable("boardId") Long boardId,
                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            Page<CommentResponse> commentResponses = freeBoardCommentServiceImpl.getCommentsForBoard(boardId, page);
            return commentResponses;
        } else {
            return null;
        }
    }

    /**
     * 특정 댓글 좋아요 여부
     *
     * @param commentId
     * @return
     */
    @GetMapping("/api/boards/{commentId}/comments/like")
    public ResponseEntity<?> getCommentLikes(@PathVariable(name = "commentId") Long commentId,
                                             @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            try{
                FreeBoardLikeResponse freeBoardLikeResponse = freeBoardCommentServiceImpl.getCommentLikes(commentId);
                return ResponseEntity.status(HttpStatus.OK).body(freeBoardLikeResponse);
            } catch (IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }


    // U(Update)
    /**
     * 댓글 업데이트
     *
     * @param commentId 댓글의 고유 식별자
     * @param dto       업데이트할 댓글 정보가 담긴 DTO
     * @return          HTTP 응답(ResponseEntity) 객체
     */
    @PutMapping("/api/boards/{commentId}/comments")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest dto,
                                           @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            try{
                freeBoardCommentServiceImpl.updateComment(commentId, dto);
                return ResponseEntity.status(HttpStatus.OK).body("댓글 수정완료");
            } catch (IllegalArgumentException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } else {
            return null;
        }
    }

    // D(Delete)
    /**
     * 댓글 삭제
     *
     * @param commentId 삭제할 댓글의 고유 식별자
     * @return          HTTP 응답(ResponseEntity) 객체
     */
    @DeleteMapping("/api/boards/{commentId}/comments")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId,
                                           @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            try{
                freeBoardCommentServiceImpl.deleteComment(commentId);
                return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제완료");
            } catch (IllegalArgumentException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 특정 댓글 좋아요 취소
     *
     * @param commentId
     * @return
     */
    @DeleteMapping("/api/boards/{commentId}/comments/like")
    public ResponseEntity<?> deleteCommentLike(@PathVariable(name = "commentId") Long commentId,
                                               @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            try{
                freeBoardCommentServiceImpl.deleteCommentLike(commentId);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }
}
