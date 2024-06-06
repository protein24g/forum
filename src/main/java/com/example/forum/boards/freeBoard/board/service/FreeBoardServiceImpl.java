package com.example.forum.boards.freeBoard.board.service;

import com.example.forum.base.board.dto.BoardSearch;
import com.example.forum.base.board.service.BoardService;
import com.example.forum.base.image.entity.Image;
import com.example.forum.base.image.service.ImageService;
import com.example.forum.boards.freeBoard.board.dto.requests.FreeBoardRequest;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.board.repository.FreeBoardRepository;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
import com.example.forum.user.dto.requests.CustomUserDetails;
import com.example.forum.user.entity.User;
import com.example.forum.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FreeBoardServiceImpl implements BoardService {
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;
    private final UserRepository userRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final ImageService imageService;

    // C(Create)
    @Override
    public FreeBoardResponse createBoard(FreeBoardRequest dto) {
        System.out.println("서비스 함수 : " + dto.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

            FreeBoard freeBoardEntity = FreeBoard.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .view(0)
                    .build();
            System.out.println("엔티티 생성완료");
            try {
                System.out.println("이미지" + dto.getImages());
                System.out.println("이미지 사이즈" + dto.getImages().size());
                List<Image> images = imageService.saveImage(dto.getImages());
                for(Image image : images){
                    freeBoardEntity.addImage(image);
                    System.out.println(image.toString());
                }
            } catch (Exception e) {
                System.out.println("예외발생");
                System.out.println(e.getMessage());
                throw new IllegalArgumentException(e.getMessage());
            }
            user.addBoard(freeBoardEntity);
            System.out.println("추가완료.");
            freeBoardRepository.save(freeBoardEntity);
            System.out.println("저장완료.");

            return FreeBoardResponse.builder()
                    .id(freeBoardEntity.getId())
                    .nickname(freeBoardEntity.getUser().getNickname())
                    .title(freeBoardEntity.getTitle())
                    .content(freeBoardEntity.getContent())
                    .createDate(freeBoardEntity.getCreateDate())
                    .build();
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    // R(Read)
    @Override
    public Page<FreeBoardResponse> pageBoards(BoardSearch dto) { // 게시글 리스트
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoard> boards = null;

        if (dto.getKeyword().length() != 0) {
            // 검색어가 있는 경우
            switch (dto.getOption()){
                case "1":
                    boards = freeBoardRepository.findByTitleContaining(dto.getKeyword(), pageable);
                    break;
                case "2":
                    boards = freeBoardRepository.findByContentContaining(dto.getKeyword(), pageable);
                    break;
            }
        } else {
            // 검색어가 없는 경우
            boards = freeBoardRepository.findAll(pageable);
        }

        return boards
                .map(board -> FreeBoardResponse.builder()
                        .id(board.getId())
                        // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .createDate(board.getCreateDate())
                        .commentCount(board.getFreeBoardComments().size())
                        .view(board.getView())
                        .hasImage((board.getImages().size() >= 1) ? true : false)
                        .build());
    }

    @Override
    public FreeBoardResponse getDetail(Long boardNum) {
        FreeBoard freeBoard = freeBoardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        List<String> imagesName = freeBoard.getImages().stream()
                .map(Image::getFileName)  // 이미지에서 파일 이름 추출
                .collect(Collectors.toList());  // 문자열 리스트로 변환

        return FreeBoardResponse.builder()
                .id(freeBoard.getId())
                // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                .nickname(freeBoard.getUser().getActive() ? freeBoard.getUser().getNickname() : "탈퇴한 사용자")
                .title(freeBoard.getTitle())
                .content(freeBoard.getContent())
                .createDate(freeBoard.getCreateDate())
                .commentResponses(freeBoardCommentServiceImpl.getCommentsForBoard(freeBoard.getId(), 0))
                .commentCount(freeBoard.getFreeBoardComments().size())
                .view(freeBoard.incView())
                .images(imagesName)
                .build();
    }

    @Override
    public Page<FreeBoardResponse> getBoardsForUser(Long userId, int page) { // 특정 유저 게시글 리스트
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoard> boards = freeBoardRepository.findByUserId(userId, pageable);

        return boards
                .map(board -> FreeBoardResponse.builder()
                        .id(board.getId())
                        // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .content(board.getContent())
                        .createDate(board.getCreateDate())
                        .commentCount(board.getFreeBoardComments().size())
                        .view(board.getView())
                        .hasImage((board.getImages().size() >= 1 ? true : false))
                        .build());
    }

    @Override
    public String getWriter(Long boardNum) {
        FreeBoard freeBoard = freeBoardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        return freeBoard.getUser().getNickname();
    }

    // U(Update)
    @Override
    public FreeBoardResponse edit(Long boardNum, FreeBoardRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            FreeBoard freeBoard = freeBoardRepository.findById(boardNum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if(freeBoard.getUser().getId().equals(customUserDetails.getId())){
                freeBoard.setTitle(dto.getTitle());
                freeBoard.setContent(dto.getContent());
                freeBoardRepository.save(freeBoard);
                return FreeBoardResponse.builder()
                        .id(freeBoard.getId())
                        .nickname(freeBoard.getUser().getNickname())
                        .title(freeBoard.getTitle())
                        .content(freeBoard.getContent())
                        .createDate(freeBoard.getCreateDate())
                        .build();
            }else{
                throw new IllegalArgumentException("본인이 작성한 글만 수정 가능합니다");
            }
        }else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    // D(Delete)
}
