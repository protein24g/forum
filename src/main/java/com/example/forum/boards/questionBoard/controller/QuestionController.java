package com.example.forum.boards.questionBoard.controller;

import com.example.forum.base.board.dto.BoardRequest;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 자유 게시판 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@Transactional
public class QuestionController {
    private final FreeBoardServiceImpl questionBoardServiceImpl;

    /**
     * 게시글 작성 페이지로 이동
     *
     * @return 게시글 작성 페이지 URL
     */
    @GetMapping("/questionBoard/create")
    public String createPage(){
        return null;
    }

    /**
     * 게시글 작성
     *
     * @param dto   작성할 게시글 정보를 담은 DTO
     * @param model Model 객체
     * @return 메시지 페이지 URL
     */
    @PostMapping("/questionBoard/create")
    public String createProc(@Valid BoardRequest dto, Model model) {
        try {
            if (dto.getImages() == null || dto.getImages().isEmpty()) {
                System.out.println("이미지 파일이 전송되지 않았습니다.");
            } else {
                for (MultipartFile image : dto.getImages()) {
                    System.out.println("이미지 파일 이름: " + image.getOriginalFilename());
                }
            }
            FreeBoardResponse questionBoardResponse = questionBoardServiceImpl.create(dto);
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
