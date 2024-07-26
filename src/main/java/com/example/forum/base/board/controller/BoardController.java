package com.example.forum.base.board.controller;

import com.example.forum.boards.freeBoard.board.dto.request.FreeBoardRequest;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final FreeBoardServiceImpl freeBoardServiceImpl;

    // C(Create)
    /**
     * 게시글 작성
     *
     * @param dto   게시글 작성 정보를 담은 DTO
     * @param model
     * @return 메시지 페이지 URL
     */

    @PostMapping("/boards/create")
    public String createProc(@Valid FreeBoardRequest dto, @RequestParam(name = "category") String category, Model model) {
        try {
            FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.create(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/boards/" + freeBoardResponse.getId());
            model.addAttribute("category", category);
            return "message/index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/login");
            return "message/index";
        }
    }

    // R(Read)
    /**
     * 게시글 작성 페이지로 이동
     * @param category
     * @param model
     * @return
     */
    @GetMapping("/boards/create")
    public String createPage(@RequestParam(name = "category") String category, Model model){
        model.addAttribute("category", category);
        return "boards/" + category + "/create";
    }

    /**
     * 게시글 리스트 페이지로 이동
     * 
     * @param category
     * @param model
     * @return
     */
    @GetMapping("/boards")
    public String boardPage(@RequestParam(name = "category") String category, Model model){
        model.addAttribute("category", category);
        return "boards/" + category + "/list";
    }

    /**
     * 특정 게시글의 상세 페이지로 이동
     *
     * @param boardId 게시글 ID
     * @param model   Model 객체
     * @return 상세 페이지 URL
     */
    @GetMapping("/boards/{boardId}")
    public String freeBoardDetailPage(@PathVariable("boardId") Long boardId,
                                      @RequestParam(name = "category") String category, Model model){
        try {
            String writer = freeBoardServiceImpl.getWriter(boardId);
            model.addAttribute("writer", writer);
            model.addAttribute("boardId", boardId);
            model.addAttribute("category", category);
            return "boards/" + category + "/detail";
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/boards");
            model.addAttribute("category", category);
            return "message/index";
        }
    }

    // U(Update)
    /**
     * 게시글 수정 페이지로 이동
     *
     * @param model   Model 객체
     * @param boardId 게시글 ID
     * @return 게시글 수정 페이지 URL
     */
    @PutMapping("/boards/{boardId}")
    public String updatePage(@PathVariable(name = "boardId") Long boardId,
                             @RequestParam(name = "category") String category, Model model){
        try{
            freeBoardServiceImpl.writerCheck(boardId);
            model.addAttribute("boardId", boardId);
            model.addAttribute("category", category);
            return "boards/" + category + "/update";
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/boards");
            model.addAttribute("category", category);
            return "message/index";
        }
    }
}
