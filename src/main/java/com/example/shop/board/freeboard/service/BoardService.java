package com.example.shop.board.freeboard.service;

import com.example.shop.board.freeboard.dto.requests.BoardRequest;
import com.example.shop.board.freeboard.dto.response.BoardResponse;
import com.example.shop.board.freeboard.entity.Board;
import com.example.shop.board.freeboard.repository.BoardRepository;
import com.example.shop.board.comment.service.CommentService;
import com.example.shop.user.dto.requests.CustomUserDetails;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
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
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    // C(Create)
    public BoardResponse createBoard(BoardRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

            Board boardEntity = Board.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .view(0)
                    .build();
            user.addBoard(boardEntity);
            boardRepository.save(boardEntity);

            return BoardResponse.builder()
                    .id(boardEntity.getId())
                    .nickname(boardEntity.getUser().getNickname())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .createDate(boardEntity.getCreateDate())
                    .build();
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    // R(Read)
    public Page<BoardResponse> getBoardsForUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boards = boardRepository.findByUserId(userId, pageable);

        return boards
                .map(board -> BoardResponse.builder()
                    .id(board.getId())
                    // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                    .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createDate(board.getCreateDate())
                    .commentCount(board.getComments().size())
                    .view(board.getView())
                    .build());
    }

    public Page<BoardResponse> pageBoards(String keyword, int page, String option) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boards = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색어가 있는 경우
            switch (option){
                case "1":
                    boards = boardRepository.findByTitleContaining(keyword, pageable);
                    break;
                case "2":
                    boards = boardRepository.findByContentContaining(keyword, pageable);
                    break;
            }

        } else {
            // 검색어가 없는 경우
            boards = boardRepository.findAll(pageable);
        }

        return boards
                .map(board -> BoardResponse.builder()
                    .id(board.getId())
                    // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                    .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createDate(board.getCreateDate())
                    .commentCount(board.getComments().size())
                    .view(board.getView())
                    .build());
    }

    public BoardResponse readDetail(Long boardNum) {
        Board board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        return BoardResponse.builder()
                .id(board.getId())
                // 사용자의 활성화 상태를 확인하고 비활성화된 경우 "탈퇴한 사용자"로 표시
                .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                .title(board.getTitle())
                .content(board.getContent())
                .createDate(board.getCreateDate())
                .commentResponses(commentService.getCommentsForBoard(board.getId(), 0))
                .view(board.incView())
                .build();
    }

    public String getWriter(Long boardNum) {
        Board board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        return board.getUser().getNickname();
    }

    public BoardResponse edit(Long boardNum, BoardRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            Board board = boardRepository.findById(boardNum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if(board.getUser().getId().equals(customUserDetails.getId())){
                board.setTitle(dto.getTitle());
                board.setContent(dto.getContent());
                boardRepository.save(board);
                return BoardResponse.builder()
                        .id(board.getId())
                        .nickname(board.getUser().getNickname())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .createDate(board.getCreateDate())
                        .build();
            }else{
                throw new IllegalArgumentException("본인이 작성한 글만 수정 가능합니다");
            }
        }else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }
}
