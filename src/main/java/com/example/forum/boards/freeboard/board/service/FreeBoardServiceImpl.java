package com.example.forum.boards.freeboard.board.service;

import com.example.forum.boards.freeboard.comment.service.FreeBoardCommentServiceImpl;
import com.example.forum.boards.freeboard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeboard.board.dto.requests.FreeBoardRequest;
import com.example.forum.boards.freeboard.board.dto.requests.FreeBoardSearch;
import com.example.forum.boards.freeboard.board.entity.FreeBoard;
import com.example.forum.boards.freeboard.board.repository.FreeBoardRepository;
import com.example.forum.base.board.service.BoardService;
import com.example.forum.user.dto.requests.CustomUserDetails;
import com.example.forum.user.entity.User;
import com.example.forum.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class FreeBoardServiceImpl implements BoardService {
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;
    private final UserRepository userRepository;
    private final FreeBoardRepository freeBoardRepository;

    // C(Create)
    @Override
    public FreeBoardResponse createBoard(FreeBoardRequest dto) {
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
            user.addBoard(freeBoardEntity);
            freeBoardRepository.save(freeBoardEntity);

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
    public Page<FreeBoardResponse> pageBoards(FreeBoardSearch dto) { // 게시글 리스트
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
                        .content(board.getContent())
                        .createDate(board.getCreateDate())
                        .commentCount(board.getFreeBoardComments().size())
                        .view(board.getView())
                        .build());
    }

    @Override
    public FreeBoardResponse getDetail(Long boardNum) {
        FreeBoard freeBoard = freeBoardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        return FreeBoardResponse.builder()
                .id(freeBoard.getId())
                // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                .nickname(freeBoard.getUser().getActive() ? freeBoard.getUser().getNickname() : "탈퇴한 사용자")
                .title(freeBoard.getTitle())
                .content(freeBoard.getContent())
                .createDate(freeBoard.getCreateDate())
                .commentResponses(freeBoardCommentServiceImpl.getCommentsForBoard(freeBoard.getId(), 0))
                .view(freeBoard.incView())
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
