package com.example.forum.boards.freeboard.board.controller;

import com.example.forum.boards.freeboard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeboard.board.dto.requests.FreeBoardRequest;
import com.example.forum.base.board.dto.BoardSearch;
import com.example.forum.boards.freeboard.board.service.FreeBoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class FreeBoardApiController {
    private final FreeBoardServiceImpl freeBoardServiceImpl;

    // R(Read)
    @PostMapping("/api/boards")
    public ResponseEntity<?> boardP(@RequestBody BoardSearch dto) {
        Page<FreeBoardResponse> boardResponses = freeBoardServiceImpl.pageBoards(dto); // 검색 페이징
        return ResponseEntity.status(HttpStatus.OK).body(boardResponses);
    }

    @GetMapping("/api/boards/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("boardId") Long boardId){
        try {
            FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.getDetail(boardId);
            return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // U(Update)
    @PutMapping("/api/boards/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable("boardId") Long boardId, @RequestBody FreeBoardRequest dto){
        try{
            freeBoardServiceImpl.edit(boardId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("리뷰 수정완료");
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
