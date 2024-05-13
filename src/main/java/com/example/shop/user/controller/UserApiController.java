package com.example.shop.user.controller;

import com.example.shop.user.dto.requests.JoinRequest;
import com.example.shop.user.dto.response.UserResponse;
import com.example.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.EntityResponse;

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
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<?> getUserDetail(@PathVariable("userId") Long userId,
                                        @RequestParam(name = "page", defaultValue = "0") int page){
        try{
            UserResponse userResponse = userService.getUserDetail(userId, page);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
