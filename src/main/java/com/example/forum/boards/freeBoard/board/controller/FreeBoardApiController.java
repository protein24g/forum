package com.example.forum.boards.freeBoard.board.controller;

import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.dto.requests.FreeBoardRequest;
import com.example.forum.base.board.dto.BoardSearch;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
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
    @PostMapping("/api/freeBoard")
    public ResponseEntity<?> boardP(@RequestBody BoardSearch dto) {
        Page<FreeBoardResponse> boardResponses = freeBoardServiceImpl.pageBoards(dto); // 검색 페이징
        return ResponseEntity.status(HttpStatus.OK).body(boardResponses);
    }

    @GetMapping("/api/freeBoard/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("boardId") Long boardId){
        try {
            FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.getDetail(boardId);
            return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // U(Update)
    @GetMapping("/api/update")
    public ResponseEntity<?> getBoardUpdateData(FreeBoardRequest dto, @RequestParam(name = "boardId") Long boardId){
        try{
            System.out.println(dto);
            System.out.println(dto.getOriginalImages());
            FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.getBoardUpdateData(boardId);
            return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/api/freeBoard")
    public ResponseEntity<?> updateBoard(@RequestParam("boardId") Long boardId, @ModelAttribute FreeBoardRequest dto){
        try{
            System.out.println("updateBoard 들어옴");
            freeBoardServiceImpl.update(boardId, dto);
            return ResponseEntity.status(HttpStatus.OK).body("글 수정완료");
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
