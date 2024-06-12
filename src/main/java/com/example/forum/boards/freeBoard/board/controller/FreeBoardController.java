package com.example.forum.boards.freeBoard.board.controller;

import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.dto.requests.FreeBoardRequest;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import jakarta.transaction.Transactional;
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
public class FreeBoardController {
    private final FreeBoardServiceImpl freeBoardServiceImpl;

    /**
     * 게시글 작성 페이지로 이동
     *
     * @return 게시글 작성 페이지 URL
     */
    @GetMapping("/freeBoard/create")
    public String createPage(){
        return "boards/freeBoard/create";
    }

    /**
     * 게시글 작성
     *
     * @param dto   작성할 게시글 정보를 담은 DTO
     * @param model Model 객체
     * @return 메시지 페이지 URL
     */
    @PostMapping("/freeBoard/create")
    public String createProc(FreeBoardRequest dto, Model model) {
        try {
            System.out.println("컨트롤러 내");
            if (dto.getImages() == null || dto.getImages().isEmpty()) {
                System.out.println("이미지 파일이 전송되지 않았습니다.");
            } else {
                for (MultipartFile image : dto.getImages()) {
                    System.out.println("이미지 파일 이름: " + image.getOriginalFilename());
                }
            }
            FreeBoardResponse freeBoardResponse = freeBoardServiceImpl.create(dto);
            model.addAttribute("msg", "글 작성이 완료되었습니다.");
            model.addAttribute("url", "/freeBoard/" + freeBoardResponse.getId());
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
    @GetMapping("/freeBoard")
    public String freeBoardPage() {
        return "boards/freeBoard/freeBoardList";
    }

    /**
     * 특정 게시글의 상세 페이지로 이동
     *
     * @param boardId 게시글 ID
     * @param model   Model 객체
     * @return 상세 페이지 URL
     */
    @GetMapping("/freeBoard/{boardId}")
    public String freeBoardDetailPage(@PathVariable("boardId") Long boardId, Model model){
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

    /**
     * 게시글 수정 페이지로 이동
     *
     * @param model   Model 객체
     * @param boardId 게시글 ID
     * @return 게시글 수정 페이지 URL
     */
    @PutMapping("/freeBoard/{boardId}")
    public String updatePage(Model model, @PathVariable(name = "boardId") Long boardId){
        try{
            freeBoardServiceImpl.writerCheck(boardId);
            model.addAttribute("boardId", boardId);
            return "boards/freeBoard/update";
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/");
            return "message/index";
        }
    }
}
