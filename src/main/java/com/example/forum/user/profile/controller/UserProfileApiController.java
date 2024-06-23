package com.example.forum.user.profile.controller;


import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.user.auth.dto.response.UserResponse;
import com.example.forum.user.profile.guestbook.dto.request.GuestBookRequest;
import com.example.forum.user.profile.guestbook.dto.response.GuestBookResponse;
import com.example.forum.user.profile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 유저 프로필 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class UserProfileApiController {
    private final UserProfileService userProfileService;

    /**
     * 내 정보 조회
     *
     * @return 내 정보 반환
     */
    @GetMapping("/api/myInfo")
    public ResponseEntity<?> getMyInfo(){
        try{
            UserResponse userResponse = userProfileService.getMyInfo();
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 마이페이지 게시글 조회
     *
     * @param id   사용자 ID
     * @param page 페이지 번호
     * @return 게시글 목록 반환
     */
    @GetMapping("/api/mypage/boards")
    public ResponseEntity<?> myPageBoards(@RequestParam(name = "id") String id,
                                          @RequestParam(name = "page", defaultValue = "0") int page){
        try{
            Page<FreeBoardResponse> freeBoardResponses = userProfileService.myPageBoards(id, page);
            return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponses);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 특정 사용자 상세 정보 조회
     *
     * @return 내 정보 반환
     */
    @GetMapping("/api/userinfo/{nickname}")
    public ResponseEntity<?> getUserInfo(@PathVariable("nickname") String nickname){
        try{
            UserResponse userResponse = userProfileService.getUserInfo(nickname);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 특정 사용자 게시글 조회
     *
     * @param id   사용자 ID
     * @param page 페이지 번호
     * @return 게시글 목록 반환
     */
    @GetMapping("/api/userinfo/{nickname}/boards")
    public ResponseEntity<?> myPageBoards(@PathVariable(name = "nickname") String nickname,
                                          @RequestParam(name = "id") String id,
                                          @RequestParam(name = "page", defaultValue = "0") int page){
        try{
            Page<FreeBoardResponse> freeBoardResponses = userProfileService.userInfoPageBoards(nickname, id, page);
            return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponses);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/api/userinfo/{nickname}/guestBooks")
    public ResponseEntity<?> createGuestBook(@PathVariable(name = "nickname") String nickname,
                                              @RequestBody GuestBookRequest dto){
        try{
            System.out.println("방명록 들어왔다");
            System.out.println(dto.toString());
            GuestBookResponse guestBookResponse = userProfileService.createGuestBook(nickname, dto);
            return ResponseEntity.status(HttpStatus.OK).body(guestBookResponse);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
