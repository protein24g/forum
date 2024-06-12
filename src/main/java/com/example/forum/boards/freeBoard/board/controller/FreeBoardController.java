package com.example.forum.boards.freeBoard.board.controller;

import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.dto.requests.FreeBoardRequest;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import com.example.forum.user.dto.requests.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Transactional
public class FreeBoardController {
    private final FreeBoardServiceImpl freeBoardServiceImpl;

    // C(Create)
    @GetMapping("/createP")
    public String createP(){
        return "boards/freeBoard/create";
    }

    @PostMapping("/freeBoard/create")
    public String create(FreeBoardRequest dto, Model model) {
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


    // R(Read)
    @GetMapping("/freeBoard")
    public String freeBoardP() {
        return "boards/freeBoard/freeBoardList";
    }

    @GetMapping("/freeBoard/{boardId}")
    public String freeBoardDetail(@PathVariable("boardId") Long boardId, Model model){
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

    // U(Update)
    @GetMapping("/updateP")
    public String updateP(Model model, @RequestParam(name = "boardId") Long boardId){
        try{
            freeBoardServiceImpl.writerCheck(boardId);
            return "boards/freeBoard/update";
        }catch (IllegalArgumentException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/");
            return "message/index";
        }
    }
}
