package com.example.forum.board.admin.controller;

import com.example.forum.board.admin.dto.requests.AdminSearch;
import com.example.forum.board.admin.dto.response.AdminResponse;
import com.example.forum.user.dto.response.UserResponse;
import com.example.forum.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminApiController {
    private final UserService userService;

    // R(Read)
    @PostMapping("/api/admin/users")
    public ResponseEntity<?> getAllUsers(@RequestBody AdminSearch dto){
        try{
            Page<AdminResponse> users = userService.getAllUsersForAdmin(dto.getKeyword(), dto.getPage(), dto.getOption());
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/api/admin/users/{userId}/boards") // 작성글 댓글 따로 만드는중
    public ResponseEntity<?> getUserBoards(@PathVariable("userId") Long userId,
                                           @RequestParam(name = "page", defaultValue = "0") int page){
        try{
            UserResponse userResponse = userService.getUserBoards(userId, page);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/api/admin/users/{userId}/comments") // 작성글 댓글 따로 만드는중
    public ResponseEntity<?> getUserComments(@PathVariable("userId") Long userId,
                                             @RequestParam(name = "page", defaultValue = "0") int page){
        try{
            UserResponse userResponse = userService.getUserComments(userId, page);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
