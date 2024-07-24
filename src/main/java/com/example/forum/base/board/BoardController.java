package com.example.forum.base.board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoardController {
    @GetMapping("/boards")
    public String boardPage(@RequestParam(name = "category") String category, Model model){
        if (category.equals("free")) {
            return "boards/freeBoard/list";
        } else {
            return null;
        }
    }
}
