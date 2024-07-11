package com.example.forum.boards.freeBoard.board.service;

import com.example.forum.base.board.service.BoardService;
import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.boards.freeBoard.board.dto.request.FreeBoardRequest;
import com.example.forum.boards.freeBoard.board.dto.request.FreeBoardSearch;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardLikeResponse;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.board.repository.FreeBoardRepository;
import com.example.forum.boards.freeBoard.comment.repository.FreeBoardCommentRepository;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
import com.example.forum.boards.freeBoard.image.entity.FreeBoardImage;
import com.example.forum.boards.freeBoard.image.repository.FreeBoardImageRepository;
import com.example.forum.boards.freeBoard.image.service.FreeBoardImageService;
import com.example.forum.user.auth.dto.requests.CustomUserDetails;
import com.example.forum.user.auth.repository.UserAuthRepository;
import com.example.forum.user.entity.User;
import com.example.forum.user.entity.UserLike;
import com.example.forum.user.profile.repository.UserLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 자유 게시판 서비스
 */
@Service
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements BoardService<FreeBoard, FreeBoardRequest, FreeBoardResponse, FreeBoardSearch> {
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;
    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final UserAuthRepository userAuthRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardImageService freeBoardImageService;
    private final FreeBoardImageRepository freeBoardImageRepository;
    private final AuthenticationService authenticationService;
    private final UserLikeRepository userLikeRepository;

    /**
     * 게시글 생성
     *
     * @param dto 게시글 요청 DTO
     * @return 생성된 게시글 응답 DTO
     */
    @Override
    @Transactional
    public FreeBoardResponse create(FreeBoardRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();

        if(customUserDetails != null){
            User user = userAuthRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            FreeBoard freeBoard = FreeBoard.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .view(0)
                    .build();
            user.addBoard(freeBoard);
            try {
                // 이미지 저장
                if(dto.getImages() != null && !dto.getImages().isEmpty()) {
                    List<FreeBoardImage> freeBoardImages = freeBoardImageService.saveImages(dto.getImages());
                    freeBoardImages.forEach(freeBoard::addImage);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
            }
            freeBoardRepository.save(freeBoard);

            return FreeBoardResponse.builder()
                    .id(freeBoard.getId())
                    .nickname(freeBoard.getUser().getNickname())
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .createDate(freeBoard.getCreateDate())
                    .build();
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 게시글 목록 조회
     *
     * @param dto 검색 조건 DTO
     * @return 게시글 페이지 응답 DTO
     */
    public Page<FreeBoardResponse> boardPage(FreeBoardSearch dto) {
        Pageable pageable;
        Sort sort;
        Page<FreeBoard> boards;

        switch (dto.getSortNum()) {
            case 0: // 날짜순
                sort = Sort.by(Sort.Direction.DESC, "id");
                break;
            case 1: // 조회순
                sort = Sort.by(Sort.Direction.DESC, "view").and(Sort.by(Sort.Direction.DESC, "id"));
                break;
            case 2: // 댓글순
                pageable = PageRequest.of(dto.getPage(), dto.getPageSize());
                if (dto.getKeyword().length() != 0) {
                    if (dto.getOption().equals("1")) {
                        boards = freeBoardRepository.findAllByTitleOrderByCommentCountDesc(dto.getKeyword(), pageable);
                    } else {
                        boards = freeBoardRepository.findAllByContentOrderByCommentCountDesc(dto.getKeyword(), pageable);
                    }
                } else {
                    boards = freeBoardRepository.findAllOrderByCommentCountDesc(pageable);
                }
                return mapToFreeBoardResponse(boards);
            case 3: // 좋아요순
                pageable = PageRequest.of(dto.getPage(), dto.getPageSize());
                if (dto.getKeyword().length() != 0) {
                    if (dto.getOption().equals("1")) {
                        boards = freeBoardRepository.findAllByTitleOrderByLikeCountDesc(dto.getKeyword(), pageable);
                    } else {
                        boards = freeBoardRepository.findAllByContentOrderByLikeCountDesc(dto.getKeyword(), pageable);
                    }
                } else {
                    boards = freeBoardRepository.findAllOrderByLikeCountDesc(pageable);
                }
                return mapToFreeBoardResponse(boards);
            default:
                throw new IllegalArgumentException("잘못된 정렬 번호: " + dto.getSortNum());
        }

        pageable = PageRequest.of(dto.getPage(), dto.getPageSize(), sort);
        if (dto.getKeyword().length() != 0) {
            if (dto.getOption().equals("1")) {
                boards = freeBoardRepository.findByTitleContaining(dto.getKeyword(), pageable);
            } else {
                boards = freeBoardRepository.findByContentContaining(dto.getKeyword(), pageable);
            }
        } else {
            boards = freeBoardRepository.findAll(pageable);
        }
        return mapToFreeBoardResponse(boards);
    }

    private Page<FreeBoardResponse> mapToFreeBoardResponse(Page<FreeBoard> boards) {
        return boards.map(board -> FreeBoardResponse.builder()
                .id(board.getId())
                .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                .title(board.getTitle())
                .createDate(board.getCreateDate())
                .commentCount(freeBoardCommentRepository.getPostCommentCount(board.getId()))
                .view(board.getView())
                .likes(board.getUserLikes().size())
                .build());
    }

    /**
     * 게시글 상세 조회
     *
     * @param boardId 게시글 ID
     * @return 게시글 응답 DTO
     */
    @Override
    @Transactional
    public FreeBoardResponse getDetail(Long boardId) {
        FreeBoard board = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        List<String> imagesName = board.getImages().stream()
                .map(FreeBoardImage::getFileName)
                .collect(Collectors.toList());
        return FreeBoardResponse.builder()
                .id(board.getId())
                .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                .title(board.getTitle())
                .content(board.getContent())
                .createDate(board.getCreateDate())
                .commentResponses(freeBoardCommentServiceImpl.getCommentsForBoard(board.getId(), 0))
                .commentCount(freeBoardCommentRepository.getPostCommentCount(board.getId()))
                .view(board.incView())
                .images(imagesName)
                .likes(board.getUserLikes().size())
                .build();
    }

    /**
     * 특정 사용자의 게시글 목록 조회
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @return 게시글 페이지 응답 DTO
     */
    @Override
    public Page<FreeBoardResponse> getBoardsForUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoard> boards = freeBoardRepository.findByUserId(userId, pageable);

        return boards
                .map(board -> FreeBoardResponse.builder()
                        .id(board.getId())
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .content(board.getContent())
                        .createDate(board.getCreateDate())
                        .commentCount(freeBoardCommentRepository.getPostCommentCount(board.getId()))
                        .view(board.getView())
                        .hasImage(!board.getImages().isEmpty())
                        .likes(board.getUserLikes().size())
                        .build());
    }

    /**
     * 게시글 작성자 닉네임 조회
     *
     * @param boardId 게시글 ID
     * @return 작성자 닉네임
     */
    @Override
    public String getWriter(Long boardId) {
        FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        return freeBoard.getUser().getNickname();
    }

    /**
     * 내가 쓴 글인지 체크
     *
     * @param boardId 게시글 ID
     */
    public void writerCheck(Long boardId) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if (!customUserDetails.getId().equals(freeBoard.getUser().getId())) {
                throw new IllegalArgumentException("글 작성자만 수정 가능합니다");
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 게시글 수정 데이터 가져오기
     *
     * @param boardId 게시글 ID
     * @return 수정할 게시글 응답 DTO
     */
    public FreeBoardResponse getBoardUpdateData(Long boardId) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if (user.getLoginId().equals(freeBoard.getUser().getLoginId())) {
                List<String> imagesName = freeBoard.getImages().stream()
                        .map(FreeBoardImage::getOriginalName)
                        .collect(Collectors.toList());
                return FreeBoardResponse.builder()
                        .title(freeBoard.getTitle())
                        .content(freeBoard.getContent())
                        .images(imagesName)
                        .build();
            } else {
                throw new IllegalArgumentException("작성자만 수정 가능합니다");
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 게시글 수정
     *
     * @param boardId 게시글 ID
     * @param dto     게시글 요청 DTO
     * @return 수정된 게시글 응답 DTO
     */
    @Override
    @Transactional
    public FreeBoardResponse update(Long boardId, FreeBoardRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if (customUserDetails == null) {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }

        FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        if (!freeBoard.getUser().getId().equals(customUserDetails.getId())) {
            throw new IllegalArgumentException("본인이 작성한 글만 수정 가능합니다");
        }

        freeBoard.setTitle(dto.getTitle());
        freeBoard.setContent(dto.getContent());

        // 기존 이미지 처리
        List<String> originalImageNames = dto.getOriginalImages();
        List<FreeBoardImage> dbImages = freeBoardImageRepository.findByFreeBoardId(boardId);
        for (FreeBoardImage dbImage : dbImages) {
            if (originalImageNames == null || !originalImageNames.contains(dbImage.getOriginalName())) {
                freeBoardImageRepository.delete(dbImage);
            }
        }

        // 새로운 이미지 저장
        try {
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                List<FreeBoardImage> newImages = freeBoardImageService.saveImages(dto.getImages());
                for (FreeBoardImage image : newImages) {
                    freeBoard.addImage(image);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }

        freeBoardRepository.save(freeBoard);

        return FreeBoardResponse.builder()
                .id(freeBoard.getId())
                .nickname(freeBoard.getUser().getNickname())
                .title(freeBoard.getTitle())
                .content(freeBoard.getContent())
                .createDate(freeBoard.getCreateDate())
                .build();
    }

    /**
     * 게시글 삭제
     *
     * @param boardId 게시글 ID
     */
    @Override
    public void delete(Long boardId){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));
            if(customUserDetails.getId().equals(freeBoard.getUser().getId())){
                freeBoardRepository.delete(freeBoard);
            }else{
                throw new IllegalArgumentException("글 작성자만 삭제 가능합니다");
            }
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 특정 게시글 좋아요 여부
     *
     * @param boardId
     * @return
     */
    public FreeBoardLikeResponse getBoardLikes(Long boardId){
        FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

            return FreeBoardLikeResponse.builder()
                    .res(user.getUserLikes().stream()
                            .anyMatch(userLike -> freeBoard.getUserLikes().contains(userLike)))
                    .likes(freeBoard.getUserLikes().size())
                    .build();
        } else {
            return FreeBoardLikeResponse.builder()
                    .res(false)
                    .likes(freeBoard.getUserLikes().size())
                    .build();
        }
    }

    /**
     * 특정 게시물 좋아요
     *
     * @param boardId
     */
    public void insertBoardLike(Long boardId){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            FreeBoardLikeResponse freeBoardLikeResponse = getBoardLikes(boardId);
            if(freeBoardLikeResponse.isRes()){
                throw new IllegalArgumentException("이미 좋아요한 게시글입니다.");
            }

            // 좋아요 추가
            UserLike userLike = new UserLike();
            user.addUserLikes(userLike);
            freeBoard.addUserLikes(userLike);
            userLikeRepository.save(userLike);
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    /**
     * 특정 게시물 좋아요 취소
     *
     * @param boardId
     * @return
     */
    @DeleteMapping("/api/freeBoard/{boardId}/like")
    public void deleteBoardLike(Long boardId){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            FreeBoardLikeResponse freeBoardLikeResponse = getBoardLikes(boardId);
            if(!freeBoardLikeResponse.isRes()){
                throw new IllegalArgumentException("이미 좋아요를 취소한 게시글입니다.");
            }

            // 좋아요 삭제
            UserLike userLike = userLikeRepository.findPostsILiked(user, boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));
            userLikeRepository.delete(userLike);
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }
}
