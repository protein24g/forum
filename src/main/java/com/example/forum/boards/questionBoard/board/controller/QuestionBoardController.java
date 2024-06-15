package com.example.forum.boards.questionBoard.board.controller;

import com.example.forum.boards.questionBoard.board.dto.request.QuestionBoardRequest;
import com.example.forum.boards.questionBoard.board.dto.response.QuestionBoardResponse;
import com.example.forum.boards.questionBoard.board.service.QuestionBoardServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 자유 게시판 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@Transactional
public class QuestionBoardController {
    private final QuestionBoardServiceImpl questionBoardServiceImpl;

    /**
     * 게시글 작성 페이지로 이동
     *
     * @return 게시글 작성 페이지 URL
     */
    @GetMapping("/questionBoard/create")
    public String createPage(){
        return "boards/questionBoard/create";
    }

    /**
     * 게시글 작성
     *
     * @param dto   작성할 게시글 정보를 담은 DTO
     * @param model Model 객체
     * @return 메시지 페이지 URL
     */

    @PostMapping("/questionBoard/create")
    public String createProc(@Valid QuestionBoardRequest dto, Model model) {
        try {
            System.out.println("질문글 dto");
            System.out.println(dto.toString());
            QuestionBoardResponse questionBoardResponse = questionBoardServiceImpl.create(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/questionBoard/" + questionBoardResponse.getId());
            return "message/index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/login");
            return "message/index";
        }
    }


    /**
     * 자유 게시판 목록 페이지로 이동
     *
     * @return 자유 게시판 목록 페이지 URL
     */
    @GetMapping("/questionBoard")
    public String questionBoardPage() {
        return "boards/questionBoard/list";
    }

    /**
     * 특정 게시글의 상세 페이지로 이동
     *
     * @param boardId 게시글 ID
     * @param model   Model 객체
     * @return 상세 페이지 URL
     */
    @GetMapping("/questionBoard/{boardId}")
    public String questionBoardDetailPage(@PathVariable("boardId") Long boardId, Model model){
        try {
            String writer = questionBoardServiceImpl.getWriter(boardId);
            model.addAttribute("writer", writer);
            model.addAttribute("boardId", boardId);
            return "boards/questionBoard/detail";
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/boards");
            return "message/index";
        }
    }

    /**
     * 게시글 수정 페이지로 이동
     *
     * @param model   Model 객체
     * @param boardId 게시글 ID
     * @return 게시글 수정 페이지 URL
     */
    @PutMapping("/questionBoard/{boardId}")
    public String updatePage(Model model, @PathVariable(name = "boardId") Long boardId){
        try{
            questionBoardServiceImpl.writerCheck(boardId);
            model.addAttribute("boardId", boardId);
            return "boards/questionBoard/update";
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/");
            return "message/index";
        }
    }
}
