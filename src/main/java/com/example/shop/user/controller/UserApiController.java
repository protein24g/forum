package com.example.shop.user.controller;

import com.example.shop.user.dto.response.UserResponse;
import com.example.shop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

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
