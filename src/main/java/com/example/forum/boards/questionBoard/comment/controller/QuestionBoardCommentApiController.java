package com.example.forum.boards.questionBoard.comment.controller;

import com.example.forum.base.comment.dto.request.CommentRequest;
import com.example.forum.base.comment.dto.response.CommentResponse;
import com.example.forum.boards.questionBoard.comment.service.QuestionBoardCommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class QuestionBoardCommentApiController {
    private final QuestionBoardCommentServiceImpl questionBoardCommentServiceImpl;

    /**
     * 특정 게시글에 댓글 생성
     *
     * @param boardId 게시글의 고유 식별자
     * @param dto     생성할 댓글 정보가 담긴 DTO
     * @return        HTTP 응답(ResponseEntity) 객체
     */
    @PostMapping("/api/questionBoard/{boardId}/comments")
    public ResponseEntity<?> createCommentForBoard(@PathVariable("boardId") Long boardId, @RequestBody CommentRequest dto){
        try{
            questionBoardCommentServiceImpl.createCommentForBoard(boardId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 생성완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 특정 게시글에 작성된 댓글 목록을 페이지 단위로 반환
     *
     * @param boardId 게시글의 고유 식별자
     * @param page    페이지 번호
     * @return        댓글 목록 페이지
     */
    @GetMapping("/api/questionBoard/{boardId}/comments")
    public Page<CommentResponse> getCommentsForBoard(@PathVariable("boardId") Long boardId,
                                                     @RequestParam(name = "page", defaultValue = "0") int page){
        Page<CommentResponse> commentResponses = questionBoardCommentServiceImpl.getCommentsForBoard(boardId, page);
        return commentResponses;
    }

    /**
     * 댓글 업데이트
     *
     * @param commentId 댓글의 고유 식별자
     * @param dto       업데이트할 댓글 정보가 담긴 DTO
     * @return          HTTP 응답(ResponseEntity) 객체
     */
    @PutMapping("/api/questionBoard/{commentId}/comments")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest dto){
        try{
            questionBoardCommentServiceImpl.updateComment(commentId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 수정완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 삭제할 댓글의 고유 식별자
     * @return          HTTP 응답(ResponseEntity) 객체
     */
    @DeleteMapping("/api/questionBoard/{commentId}/comments")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId){
        try{
            questionBoardCommentServiceImpl.deleteComment(commentId);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제완료");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
