package com.example.shop.board.admin.controller;

import com.example.shop.board.admin.dto.requests.AdminSearch;
import com.example.shop.board.admin.dto.response.AdminResponse;
import com.example.shop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminApiController {
    private final UserService userService;

    @PostMapping("/api/admin/users")
    public ResponseEntity<?> getAllUsers(@RequestBody AdminSearch dto){
        try{
            Page<AdminResponse> users = userService.getAllUsersForAdmin(dto.getKeyword(), dto.getPage(), dto.getOption());
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
