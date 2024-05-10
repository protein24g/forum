package com.example.shop.board.freeboard.controller;

import com.example.shop.board.freeboard.dto.requests.BoardRequest;
import com.example.shop.board.freeboard.dto.response.BoardResponse;
import com.example.shop.board.freeboard.service.BoardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Transactional
public class BoardController {
    private final BoardService boardService;

    // C(Create)
    @GetMapping("/boards/create")
    public String createP(){
        return "board/create";
    }

    @PostMapping("/boards/create")
    public String createBoard(BoardRequest dto, Model model){
        try {
            BoardResponse boardResponse = boardService.createBoard(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/boards/" + boardResponse.getId());
            return "message/main";

        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/login");
            return "message/main";
        }
    }

    // R(Read)
    @GetMapping("/boards")
    public String listBoards() {
        return "/board/list";
    }

    @GetMapping("/boards/{boardId}")
    public String showBoardDetail(@PathVariable("boardId") Long boardId, Model model){
        try {
            String writer = boardService.getWriter(boardId);
            model.addAttribute("writer", writer);
            model.addAttribute("boardId", boardId);
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/qna");
            return "message/main";
        }
        return "board/detail";
    }
}
