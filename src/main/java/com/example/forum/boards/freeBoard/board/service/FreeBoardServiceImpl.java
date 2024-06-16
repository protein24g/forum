package com.example.forum.boards.freeBoard.board.service;

import com.example.forum.base.board.service.BoardService;
import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.boards.freeBoard.board.dto.request.FreeBoardRequest;
import com.example.forum.boards.freeBoard.board.dto.request.FreeBoardSearch;
import com.example.forum.boards.freeBoard.board.dto.response.FreeBoardResponse;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.board.repository.FreeBoardRepository;
import com.example.forum.boards.freeBoard.comment.service.FreeBoardCommentServiceImpl;
import com.example.forum.boards.freeBoard.image.entity.FreeBoardImage;
import com.example.forum.boards.freeBoard.image.entity.FreeBoardThumbnail;
import com.example.forum.boards.freeBoard.image.repository.FreeBoardImageRepository;
import com.example.forum.boards.freeBoard.image.repository.FreeBoardThumbnailRepository;
import com.example.forum.boards.freeBoard.image.service.FreeBoardImageService;
import com.example.forum.boards.freeBoard.image.service.FreeBoardThumbnailImageService;
import com.example.forum.user.dto.requests.CustomUserDetails;
import com.example.forum.user.entity.User;
import com.example.forum.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 자유 게시판 서비스 구현 클래스
 */
@Service
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements BoardService<FreeBoard, FreeBoardRequest, FreeBoardResponse, FreeBoardSearch> {
    private final FreeBoardCommentServiceImpl freeBoardCommentServiceImpl;
    private final UserRepository userRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardImageService freeBoardImageService;
    private final FreeBoardThumbnailImageService freeBoardThumbnailImageService;
    private final FreeBoardImageRepository freeBoardImageRepository;
    private final FreeBoardThumbnailRepository freeBoardThumbnailRepository;
    private final AuthenticationService authenticationService;

    /**
     * 게시글 생성
     *
     * @param dto 게시글 요청 DTO
     * @return 생성된 게시글 응답 DTO
     */
    @Override
    public FreeBoardResponse create(FreeBoardRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();

        if(customUserDetails != null){
            User user = userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

            FreeBoard freeBoard = FreeBoard.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .view(0)
                    .build();
            try {
                // 썸네일 이미지 저장
                if(dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()){
                    FreeBoardThumbnail freeBoardThumbnail = freeBoardThumbnailImageService.saveImage(dto.getThumbnail());
                    freeBoard.addThumbnail(freeBoardThumbnail);
                }

                // 이미지 저장
                if(dto.getImages() != null && !dto.getImages().isEmpty()) {
                    List<FreeBoardImage> freeBoardImages = freeBoardImageService.saveImages(dto.getImages());
                    for (FreeBoardImage image : freeBoardImages) {
                        freeBoard.addImage(image);
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
            }

            user.addBoard(freeBoard);
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
    @Override
    @Transactional
    public Page<FreeBoardResponse> boardPage(FreeBoardSearch dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeBoard> boards;

        if (dto.getKeyword().length() != 0) {
            switch (dto.getOption()) {
                case "1":
                    boards = freeBoardRepository.findByTitleContaining(dto.getKeyword(), pageable);
                    break;
                case "2":
                    boards = freeBoardRepository.findByContentContaining(dto.getKeyword(), pageable);
                    break;
                default:
                    boards = freeBoardRepository.findAll(pageable);
            }
        } else {
            boards = freeBoardRepository.findAll(pageable);
        }

        return boards
                .map(board -> FreeBoardResponse.builder()
                        .id(board.getId())
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .createDate(board.getCreateDate())
                        .commentCount(board.getFreeBoardComments().size())
                        .view(board.getView())
                        .thumbnail((board.getThumbnail() != null) ? board.getThumbnail().getFileName() : null)
                        .hasImage(board.getThumbnail() != null)
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
        FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        List<String> imagesName = freeBoard.getImages().stream()
                .map(FreeBoardImage::getFileName)
                .collect(Collectors.toList());
        return FreeBoardResponse.builder()
                .id(freeBoard.getId())
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
                        .commentCount(board.getFreeBoardComments().size())
                        .view(board.getView())
                        .hasImage(!board.getImages().isEmpty())
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
            User user = userRepository.findById(customUserDetails.getId())
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
    public FreeBoardResponse update(Long boardId, FreeBoardRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            FreeBoard freeBoard = freeBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if (freeBoard.getUser().getId().equals(customUserDetails.getId())) {
                freeBoard.setTitle(dto.getTitle());
                freeBoard.setContent(dto.getContent());

                // 썸네일 이미지 수정
                if(dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()){
                    List<FreeBoardThumbnail> freeBoardThumbnail = freeBoardThumbnailRepository.findByFreeBoardId(boardId);
                    freeBoardThumbnailRepository.delete(freeBoardThumbnail.get(0));
                    try{
                        FreeBoardThumbnail freeBoardThumbnail1 = freeBoardThumbnailImageService.saveImage(dto.getThumbnail());
                        freeBoard.addThumbnail(freeBoardThumbnail1);
                    } catch (Exception e){
                        throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
                    }
                }

                List<String> originalImageNames = dto.getOriginalImages();
                List<FreeBoardImage> dbImages = freeBoardImageRepository.findByFreeBoardId(boardId);

                // 기존 이미지 처리 로직
                for (FreeBoardImage freeBoardImage : dbImages) {
                    if(originalImageNames == null || !originalImageNames.contains(freeBoardImage.getOriginalName())){
                        freeBoardImageRepository.delete(freeBoardImage);
                    }
                }

                // 새로운 이미지 저장 로직
                try {
                    if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                        List<FreeBoardImage> freeBoardImages = freeBoardImageService.saveImages(dto.getImages());
                        for (FreeBoardImage image : freeBoardImages) {
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
            } else {
                throw new IllegalArgumentException("본인이 작성한 글만 수정 가능합니다");
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
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
}
