package com.example.forum.user.controller;

import com.example.forum.user.dto.requests.JoinRequest;
import com.example.forum.user.service.UserService;
import lombok.RequiredArgsConstructor;
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
}
