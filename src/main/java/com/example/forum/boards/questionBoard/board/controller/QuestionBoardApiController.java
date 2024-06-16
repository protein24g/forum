package com.example.forum.boards.questionBoard.board.controller;

import com.example.forum.boards.questionBoard.board.dto.request.QuestionBoardRequest;
import com.example.forum.boards.questionBoard.board.dto.request.QuestionBoardSearch;
import com.example.forum.boards.questionBoard.board.dto.response.QuestionBoardResponse;
import com.example.forum.boards.questionBoard.board.service.QuestionBoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * 자유 게시판 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class QuestionBoardApiController {
    private final QuestionBoardServiceImpl questionBoardServiceImpl;

    /**
     * 게시글 목록을 검색하여 반환
     *
     * @param dto 검색 조건을 포함한 DTO
     * @return 검색된 게시글 목록 페이지
     */
    @PostMapping("/api/questionBoard")
    public ResponseEntity<?> boardPage(@RequestBody QuestionBoardSearch dto) {
        Page<QuestionBoardResponse> questionBoardResponses = questionBoardServiceImpl.boardPage(dto); // 검색 페이징
        return ResponseEntity.status(HttpStatus.OK).body(questionBoardResponses);
    }

    /**
     * 특정 게시글의 상세 정보 반환
     *
     * @param boardId 게시글 ID
     * @return 게시글 상세 정보
     */
    @GetMapping("/api/questionBoard/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("boardId") Long boardId){
        try {
            QuestionBoardResponse questionBoardResponse = questionBoardServiceImpl.getDetail(boardId);
            return ResponseEntity.status(HttpStatus.OK).body(questionBoardResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 게시글 수정을 위한 데이터 반환
     *
     * @param boardId 게시글 ID
     * @return 게시글 수정을 위한 데이터
     */
    @GetMapping("/api/questionBoard/{boardId}/update")
    public ResponseEntity<?> getBoardUpdateData(@PathVariable(name = "boardId") Long boardId){
        try{
            QuestionBoardResponse questionBoardResponse = questionBoardServiceImpl.getBoardUpdateData(boardId);
            return ResponseEntity.status(HttpStatus.OK).body(questionBoardResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 게시글 수정
     *
     * @param boardId 게시글 ID
     * @param dto     수정할 게시글 내용을 담은 DTO
     * @return 수정 결과 메시지
     */
    @PutMapping("/api/questionBoard/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable("boardId") Long boardId, QuestionBoardRequest dto) {
        try {
            questionBoardServiceImpl.update(boardId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("글 수정완료");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 게시글 삭제
     *
     * @param boardId 게시글 ID
     */
    @DeleteMapping("/api/questionBoard/{boardId}")
    public ResponseEntity<?> delete(@PathVariable("boardId") Long boardId) {
        try {
            questionBoardServiceImpl.delete(boardId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
