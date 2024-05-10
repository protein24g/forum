package com.example.shop.board.freeboard.controller;

import com.example.shop.board.freeboard.dto.requests.BoardRequest;
import com.example.shop.board.freeboard.dto.response.BoardResponse;
import com.example.shop.board.freeboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class BoardApiController {
    private final BoardService boardService;

    // R(Read)
    @GetMapping("/api/boards")
    public ResponseEntity<?> boardP(
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "option", required = false) String option) {
        Page<BoardResponse> boardResponses;
        if (keyword != null) { // 키워드가 있으면
            boardResponses = boardService.pageBoards(keyword, page, option); // 검색 페이징
        } else {
            boardResponses = boardService.pageBoards("", page, ""); // 기본 리스트 페이징
        }
        return ResponseEntity.status(HttpStatus.OK).body(boardResponses);
    }

    @GetMapping("/api/boards/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("boardId") Long boardId){
        try {
            BoardResponse boardResponse = boardService.readDetail(boardId);
            return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // U(Update)
    @PutMapping("/api/boards/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardRequest dto){
        try{
            boardService.edit(boardId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("리뷰 수정완료");
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
