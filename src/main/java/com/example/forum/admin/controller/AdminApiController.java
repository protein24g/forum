package com.example.forum.admin.controller;

import com.example.forum.admin.dto.requests.AdminSearch;
import com.example.forum.admin.dto.response.AdminResponse;
import com.example.forum.admin.service.AdminService;
import com.example.forum.base.comment.dto.response.CommentResponse;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.service.FreeBoardServiceImpl;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
import com.example.forum.user.dto.response.UserResponse;
import com.example.forum.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * 관리자 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminApiController {
    private final AdminService adminService;
    private final FreeBoardServiceImpl freeBoardServiceImpl;
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;

    /**
     * 모든 사용자 정보를 반환
     *
     * @param dto AdminSearch DTO 객체
     * @return 모든 사용자 정보 페이지
     */
    @PostMapping("/api/admin/users")
    public ResponseEntity<?> getAllUsers(@RequestBody AdminSearch dto){
        try{
            Page<AdminResponse> users = adminService.getAllUsersForAdmin(dto.getKeyword(), dto.getPage(), dto.getOption());
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 특정 사용자가 작성한 게시글 목록 반환
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @return 특정 사용자가 작성한 게시글 정보
     */
    @GetMapping("/api/admin/users/{userId}/boards")
    public ResponseEntity<?> getUserBoards(@PathVariable("userId") Long userId,
                                           @RequestParam(name = "page", defaultValue = "0") int page){
        try{
            Page<FreeBoardResponse> freeBoardResponses = freeBoardServiceImpl.getBoardsForUser(userId, page);
            return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponses);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 특정 사용자가 작성한 댓글 목록 반환
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @return 특정 사용자가 작성한 댓글 정보
     */
    @GetMapping("/api/admin/users/{userId}/comments")
    public ResponseEntity<?> getUserComments(@PathVariable("userId") Long userId,
                                             @RequestParam(name = "page", defaultValue = "0") int page){
        try{
            Page<CommentResponse> userResponses = freeBoardCommentServiceImpl.getCommentsForUser(userId, page);
            return ResponseEntity.status(HttpStatus.OK).body(userResponses);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
