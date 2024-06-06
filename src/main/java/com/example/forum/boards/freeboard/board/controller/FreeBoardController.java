package com.example.forum.boards.freeboard.board.controller;

import com.example.forum.boards.freeboard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeboard.board.dto.requests.FreeBoardRequest;
import com.example.forum.boards.freeboard.board.service.FreeBoardServiceImpl;
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
        return "freeboard/board/create";
    }

    @PostMapping("/boards/create")
    public String createBoard(FreeBoardRequest dto, Model model){
        try {
            System.out.println("컨트롤러" + dto.toString());
            FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.createBoard(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/boards/" + freeBoardResponse.getId());
            return "message/index";

        } catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/login");
            return "message/index";
        }
    }

    // R(Read)
    @GetMapping("/boards")
    public String listBoards() {
        return "freeboard/board/list";
    }

    @GetMapping("/boards/{boardId}")
    public String showBoardDetail(@PathVariable("boardId") Long boardId, Model model){
        try {
            String writer = freeBoardServiceImpl.getWriter(boardId);
            model.addAttribute("writer", writer);
            model.addAttribute("boardId", boardId);
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/boards");
            return "message/index";
        }
        return "freeboard/board/detail";
    }
}
