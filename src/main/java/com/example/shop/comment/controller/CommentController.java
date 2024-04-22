package com.example.shop.comment.controller;

import com.example.shop.comment.dto.requests.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    @PostMapping("/qna/{boardNum}/comments")
    public String create(@PathVariable("boardNum") Long boardNum, CommentRequest commentRequest){
        System.out.println(commentRequest.toString());
        return "redirect:/qna/" + boardNum;
    }
}
