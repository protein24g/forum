package com.example.forum.base.board.controller;

import com.example.forum.boards.freeBoard.board.dto.request.FreeBoardRequest;
import com.example.forum.boards.freeBoard.board.dto.request.FreeBoardSearch;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardLikeResponse;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardPrevNextResponse;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardApiController {
    private final FreeBoardServiceImpl freeBoardServiceImpl;

    // C(Create)
    /**
     * 게시글 작성
     *
     * @param dto   게시글 작성 정보를 담은 DTO
     * @return 메시지 페이지 URL
     */

    @PostMapping("/api/boards/create")
    public ResponseEntity<?> createProc(@Valid FreeBoardRequest dto, @RequestParam(name = "category") String category) {
        if (category.equals("free")) {
            try {
                FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.create(dto);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 특정 게시글 좋아요
     *
     * @param boardId
     * @return
     */
    @PostMapping("/api/boards/{boardId}/like")
    public ResponseEntity<?> insertBoardLike(@PathVariable(name = "boardId") Long boardId,
                                             @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            try{
                freeBoardServiceImpl.insertBoardLike(boardId);
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
     * 게시글 목록을 검색하여 반환
     *
     * @param dto 검색 조건을 포함한 DTO
     * @return 검색된 게시글 목록 페이지
     */
    @PostMapping("/api/boards")
    public ResponseEntity<?> boardPage(@RequestBody FreeBoardSearch dto,
                                       @RequestParam(name = "category") String category) {
        if (category.equals("free")) {
            Page<FreeBoardResponse> freeBoardResponses = freeBoardServiceImpl.boardPage(dto); // 검색 페이징
            return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponses);
        } else {
            return null;
        }
    }

    /**
     * 특정 게시글의 상세 정보 반환
     *
     * @param boardId 게시글 ID
     * @return 게시글 상세 정보
     */
    @GetMapping("/api/boards/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("boardId") Long boardId,
                                            @RequestParam(name = "category") String category) {
        if (category.equals("free")) {
            try {
                FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.getDetail(boardId);
                return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponse);
            }catch (IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 게시글 수정을 위한 데이터 반환
     *
     * @param boardId 게시글 ID
     * @return 게시글 수정을 위한 데이터
     */
    @GetMapping("/api/boards/{boardId}/update")
    public ResponseEntity<?> getBoardUpdateData(@PathVariable(name = "boardId") Long boardId,
                                                @RequestParam(name = "category") String category) {
        if (category.equals("free")) {
            try{
                FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.getBoardUpdateData(boardId);
                return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponse);
            }catch (IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 특정 게시글 좋아요 여부
     *
     * @param boardId
     * @return
     */
    @GetMapping("/api/boards/{boardId}/like")
    public ResponseEntity<?> getBoardLikes(@PathVariable(name = "boardId") Long boardId,
                                           @RequestParam(name = "category") String category) {
        if (category.equals("free")) {
            try{
                FreeBoardLikeResponse freeBoardLikeResponse = freeBoardServiceImpl.getBoardLikes(boardId);
                return ResponseEntity.status(HttpStatus.OK).body(freeBoardLikeResponse);
            } catch (IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 이전, 다음 글의 id, 제목 반환
     * @param boardId
     * @return
     */
    @GetMapping("/api/boards/{boardId}/prev-next")
    public ResponseEntity<?> getPrevNext(@PathVariable(name = "boardId") Long boardId,
                                         @RequestParam(name = "category") String category) {
        if (category.equals("free")) {
            try {
                FreeBoardPrevNextResponse freeBoardPrevNextResponse = freeBoardServiceImpl.getPrevNext(boardId);
                return ResponseEntity.status(HttpStatus.OK).body(freeBoardPrevNextResponse);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }

    // U(Update)
    /**
     * 게시글 수정
     *
     * @param boardId 게시글 ID
     * @param dto     수정할 게시글 내용을 담은 DTO
     * @return 수정 결과 메시지
     */
    @PutMapping("/api/boards/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable("boardId") Long boardId, FreeBoardRequest dto,
                                         @RequestParam(name = "category") String category) {
        if (category.equals("free")) {
            try {
                System.out.println("게시글 수정 요청 dto");
                System.out.println(dto.getTitle());
                System.out.println(dto.getContent());
                System.out.println(dto.getOriginalImages());
                System.out.println(dto.getImages());
                freeBoardServiceImpl.update(boardId, dto);
                return ResponseEntity.status(HttpStatus.OK).body("글 수정완료");
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } else {
            return null;
        }
    }

    // D(Delete)
    /**
     * 게시글 삭제
     *
     * @param boardId 게시글 ID
     */
    @DeleteMapping("/api/boards/{boardId}")
    public ResponseEntity<?> delete(@PathVariable("boardId") Long boardId,
                                    @RequestParam(name = "category") String category) {
        if (category.equals("free")) {
            try {
                freeBoardServiceImpl.delete(boardId);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 특정 게시물 좋아요 취소
     *
     * @param boardId
     * @return
     */
    @DeleteMapping("/api/boards/{boardId}/like")
    public ResponseEntity<?> deleteBoardLike(@PathVariable(name = "boardId") Long boardId,
                                             @RequestParam(name = "category") String category){
        if (category.equals("free")) {
            try{
                freeBoardServiceImpl.deleteBoardLike(boardId);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return null;
        }
    }
}
