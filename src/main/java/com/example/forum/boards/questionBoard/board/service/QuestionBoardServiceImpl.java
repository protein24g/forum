package com.example.forum.boards.questionBoard.board.service;

import com.example.forum.base.board.dto.request.BoardSearch;
import com.example.forum.base.board.dto.response.BoardResponse;
import com.example.forum.base.board.service.BoardService;
import com.example.forum.base.image.entity.Image;
import com.example.forum.base.board.auth.AuthenticationService;
import com.example.forum.base.image.repository.ImageRepository;
import com.example.forum.base.image.service.ImageService;
import com.example.forum.base.board.dto.request.BoardRequest;
import com.example.forum.boards.questionBoard.board.entity.QuestionBoard;
import com.example.forum.boards.questionBoard.board.repository.QuestionBoardRepository;
import com.example.forum.boards.questionBoard.comment.service.QuestionBoardCommentServiceImpl;
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
public class QuestionBoardServiceImpl implements BoardService {
    private final QuestionBoardCommentServiceImpl questionBoardCommentServiceImpl;
    private final UserRepository userRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final AuthenticationService authenticationService;

    /**
     * 게시글 생성
     *
     * @param dto 게시글 요청 DTO
     * @return 생성된 게시글 응답 DTO
     */
    @Override
    public BoardResponse create(BoardRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();

        if(customUserDetails != null){
            User user = userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

            QuestionBoard questionBoard = QuestionBoard.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .view(0)
                    .build();
            try {
                if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                    List<Image> images = imageService.saveImage(dto.getImages());
                    for (Image image : images) {
                        questionBoard.addImage(image);
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
            }

            user.addBoard(questionBoard);
            questionBoardRepository.save(questionBoard);

            return BoardResponse.builder()
                    .id(questionBoard.getId())
                    .nickname(questionBoard.getUser().getNickname())
                    .title(questionBoard.getTitle())
                    .content(questionBoard.getContent())
                    .createDate(questionBoard.getCreateDate())
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
    public Page<BoardResponse> boardPage(BoardSearch dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        Page<QuestionBoard> boards;

        if (dto.getKeyword().length() != 0) {
            switch (dto.getOption()) {
                case "1":
                    boards = questionBoardRepository.findByTitleContaining(dto.getKeyword(), pageable);
                    break;
                case "2":
                    boards = questionBoardRepository.findByContentContaining(dto.getKeyword(), pageable);
                    break;
                default:
                    boards = questionBoardRepository.findAll(pageable);
            }
        } else {
            boards = questionBoardRepository.findAll(pageable);
        }

        return boards
                .map(board -> BoardResponse.builder()
                        .id(board.getId())
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .createDate(board.getCreateDate())
                        .commentCount(board.getQuestionBoardComments().size())
                        .view(board.getView())
                        .hasImage(!board.getImages().isEmpty())
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
    public BoardResponse getDetail(Long boardId) {
        QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        List<String> imagesName = questionBoard.getImages().stream()
                .map(Image::getFileName)
                .collect(Collectors.toList());

        return BoardResponse.builder()
                .id(questionBoard.getId())
                .nickname(questionBoard.getUser().getActive() ? questionBoard.getUser().getNickname() : "탈퇴한 사용자")
                .title(questionBoard.getTitle())
                .content(questionBoard.getContent())
                .createDate(questionBoard.getCreateDate())
                .commentResponses(questionBoardCommentServiceImpl.getCommentsForBoard(questionBoard.getId(), 0))
                .commentCount(questionBoard.getQuestionBoardComments().size())
                .view(questionBoard.incView())
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
    public Page<BoardResponse> getBoardsForUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<QuestionBoard> boards = questionBoardRepository.findByUserId(userId, pageable);

        return boards
                .map(board -> BoardResponse.builder()
                        .id(board.getId())
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .content(board.getContent())
                        .createDate(board.getCreateDate())
                        .commentCount(board.getQuestionBoardComments().size())
                        .view(board.getView())
                        .hasImage(!board.getImages().isEmpty())
                        .build());
    }

    /**
     * 게시글 작성자 조회
     *
     * @param boardId 게시글 ID
     * @return 작성자 닉네임
     */
    @Override
    public String getWriter(Long boardId) {
        QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        return questionBoard.getUser().getNickname();
    }

    /**
     * 게시글 작성자 확인
     *
     * @param boardId 게시글 ID
     */
    public void writerCheck(Long boardId) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if (!customUserDetails.getId().equals(questionBoard.getUser().getId())) {
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
    public BoardResponse getBoardUpdateData(Long boardId) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if (user.getLoginId().equals(questionBoard.getUser().getLoginId())) {
                List<String> imagesName = questionBoard.getImages().stream()
                        .map(Image::getOriginalName)
                        .collect(Collectors.toList());
                return BoardResponse.builder()
                        .title(questionBoard.getTitle())
                        .content(questionBoard.getContent())
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
    public BoardResponse update(Long boardId, BoardRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if (questionBoard.getUser().getId().equals(customUserDetails.getId())) {
                questionBoard.setTitle(dto.getTitle());
                questionBoard.setContent(dto.getContent());
                List<String> originalImageNames = dto.getOriginalImages();
                List<Image> dbImages = imageRepository.findByQuestionBoardId(boardId);

                // 기존 이미지 처리 로직
                for (Image image : dbImages) {
                    if(originalImageNames == null || !originalImageNames.contains(image.getOriginalName())){
                        imageRepository.delete(image);
                    }
                }

                // 새로운 이미지 저장 로직
                try {
                    if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                        List<Image> images = imageService.saveImage(dto.getImages());
                        for (Image image : images) {
                            questionBoard.addImage(image);
                        }
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
                }

                questionBoardRepository.save(questionBoard);

                return BoardResponse.builder()
                        .id(questionBoard.getId())
                        .nickname(questionBoard.getUser().getNickname())
                        .title(questionBoard.getTitle())
                        .content(questionBoard.getContent())
                        .createDate(questionBoard.getCreateDate())
                        .build();
            } else {
                throw new IllegalArgumentException("본인이 작성한 글만 수정 가능합니다");
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    // D(Delete) - 삭제 메서드 추가 필요
}
