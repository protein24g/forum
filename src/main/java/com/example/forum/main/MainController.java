package com.example.forum.main;

import com.example.forum.base.board.service.BoardService;
import com.example.forum.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/")
    public String mainP(){
        return "main/index";
    }
}
