package com.example.forum.user.controller;

import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.user.dto.requests.JoinRequest;
import com.example.forum.user.dto.response.UserResponse;
import com.example.forum.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    // C(Create)
    @PostMapping("/api/joinProc")
    public ResponseEntity<?> joinProc(@RequestBody JoinRequest dto) { // 뷰로 데이터를 전달하기 위한 Model 객체.
        try {
            userService.join(dto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // R(Read)
    @GetMapping("/api/userInfo")
    public ResponseEntity<?> getUserInfo(){
        try{
            UserResponse userResponse = userService.getUserInfo();
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/api/mypage/boards")
    public ResponseEntity<?> myPageBoards(@RequestParam(name = "id") String id,
                               @RequestParam(name = "page", defaultValue = "0") int page){
        try{
            Page<FreeBoardResponse> boardResponses = userService.myPageBoards(id, page);
            return ResponseEntity.status(HttpStatus.OK).body(boardResponses);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
