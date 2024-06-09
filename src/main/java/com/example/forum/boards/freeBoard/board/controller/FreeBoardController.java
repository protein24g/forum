package com.example.forum.boards.freeBoard.board.controller;

import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.dto.requests.FreeBoardRequest;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
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
public class FreeBoardController {
    private final FreeBoardServiceImpl freeBoardServiceImpl;

    // C(Create)
    @GetMapping("/createPage")
    public String createP(){
        return "boards/freeBoard/create";
    }

    @PostMapping("/freeBoards/create")
    public String createBoard(FreeBoardRequest dto, Model model){
        try {
            FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.createBoard(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/freeBoards/" + freeBoardResponse.getId());
            return "message/index";
        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/login");
            return "message/index";
        }
    }

    // R(Read)
    @GetMapping("/freeBoards")
    public String freeBoardsP() {
        return "boards/freeBoard/freeBoardList";
    }

    @GetMapping("/freeBoards/{boardId}")
    public String freeBoardsDetail(@PathVariable("boardId") Long boardId, Model model){
        try {
            String writer = freeBoardServiceImpl.getWriter(boardId);
            model.addAttribute("writer", writer);
            model.addAttribute("boardId", boardId);
            return "boards/freeBoard/detail";
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/boards");
            return "message/index";
        }
    }
}
