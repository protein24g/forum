package com.example.forum.user.auth.controller;

import com.example.forum.user.auth.dto.requests.JoinRequest;
import com.example.forum.user.auth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 유저 계정 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class UserAuthApiController {
    private final UserAuthService userAuthService;

    /**
     * 회원가입 처리
     *
     * @param dto JoinRequest 객체
     * @return HTTP 상태 및 메시지 반환
     */
    @PostMapping("/api/joinProc")
    public ResponseEntity<?> joinProc(@RequestBody JoinRequest dto) {
        try {
            userAuthService.join(dto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
