package com.example.forum.user.profile.controller;


import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.user.auth.dto.response.UserResponse;
import com.example.forum.user.profile.guestbook.dto.request.GuestBookRequest;
import com.example.forum.user.profile.guestbook.dto.response.GuestBookResponse;
import com.example.forum.user.profile.guestbook.dto.response.MyGuestBookResponse;
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
     * 마이페이지 내가 쓴 방명록 조회
     *
     * @param page 페이지 번호
     * @param size 페이지당 보여줄 개수
     * @return
     */
    @GetMapping("/api/mypage/guestBoards")
    public ResponseEntity<?> myPageGuestBoards(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size){
        try{
            Page<MyGuestBookResponse> guestBookResponses = userProfileService.myPageGuestBoards(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(guestBookResponses);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 마이페이지 게시글 또는 댓글 조회
     *
     * @param id   게시글 또는 댓글 식별자 ("myBoards" 또는 "myComments")
     * @param page 페이지 번호
     * @return 게시글 또는 댓글 목록 응답 페이지 DTO
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

    /**
     * 특정 사용자 방명록 작성
     *
     * @param nickname 사용자 닉네임
     * @param dto      방명록 작성 객체
     * @return
     */
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

    /**
     * 특정 사용자 페이지 방명록 글 목록 페이징 조회
     *
     * @param nickname 특정 사용자 닉네임
     * @param page     페이지
     * @param size     페이지당 보여줄 개수
     * @return
     */
    @GetMapping("/api/userinfo/{nickname}/guestBooks")
    public ResponseEntity<?> getGuestBook(@PathVariable(name = "nickname") String nickname,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "10") int size){
        Page<GuestBookResponse> freeBoardResponses = userProfileService.userInfoGuestBooks(nickname, page, size); // 검색 페이징
        return ResponseEntity.status(HttpStatus.OK).body(freeBoardResponses);
    }

    /**
     * 방명록 수정
     *
     * @param dto 방명록 수정 데이터를 담은 객체
     * @return
     */
    @PutMapping("/api/userinfo/guestBooks")
    public ResponseEntity<?> putGuestBook(@RequestBody GuestBookRequest dto){
        try{
            userProfileService.putGuestBook(dto.getId(), dto.getContent());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 방명록 삭제
     *
     * @param dto 방명록 삭제 데이터를 담은 객체
     * @return
     */
    @DeleteMapping("/api/userinfo/guestBooks")
    public ResponseEntity<?> deleteGuestBook(@RequestBody GuestBookRequest dto){
        try{
            userProfileService.deleteGuestBook(dto.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
