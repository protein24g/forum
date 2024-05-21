package com.example.forum.board.freeboard.controller;

import com.example.forum.board.freeboard.dto.requests.BoardRequest;
import com.example.forum.board.freeboard.dto.requests.BoardSearch;
import com.example.forum.board.freeboard.dto.response.BoardResponse;
import com.example.forum.board.freeboard.service.BoardService;
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
    @PostMapping("/api/boards")
    public ResponseEntity<?> boardP(@RequestBody BoardSearch dto) {
        System.out.println(dto.toString());
        Page<BoardResponse> boardResponses = boardService.pageBoards(dto); // 검색 페이징
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
