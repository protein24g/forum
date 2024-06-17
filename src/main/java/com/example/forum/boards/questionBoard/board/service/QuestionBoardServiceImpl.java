package com.example.forum.boards.questionBoard.board.service;

import com.example.forum.base.board.service.BoardService;
import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.boards.questionBoard.board.dto.request.QuestionBoardRequest;
import com.example.forum.boards.questionBoard.board.dto.request.QuestionBoardSearch;
import com.example.forum.boards.questionBoard.board.dto.response.QuestionBoardResponse;
import com.example.forum.boards.questionBoard.board.entity.QuestionBoard;
import com.example.forum.boards.questionBoard.board.repository.QuestionBoardRepository;
import com.example.forum.boards.questionBoard.comment.service.QuestionBoardCommentServiceImpl;
import com.example.forum.boards.questionBoard.image.entity.QuestionBoardImage;
import com.example.forum.boards.questionBoard.image.entity.QuestionBoardThumbnail;
import com.example.forum.boards.questionBoard.image.repository.QuestionBoardImageRepository;
import com.example.forum.boards.questionBoard.image.repository.QuestionBoardThumbnailRepository;
import com.example.forum.boards.questionBoard.image.service.QuestionBoardImageService;
import com.example.forum.boards.questionBoard.image.service.QuestionBoardThumbnailImageService;
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
public class QuestionBoardServiceImpl implements BoardService<QuestionBoard, QuestionBoardRequest, QuestionBoardResponse, QuestionBoardSearch> {
    private final QuestionBoardCommentServiceImpl questionBoardCommentServiceImpl;
    private final UserRepository userRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardImageService questionBoardImageService;
    private final QuestionBoardThumbnailImageService questionBoardThumbnailImageService;
    private final QuestionBoardImageRepository questionBoardImageRepository;
    private final QuestionBoardThumbnailRepository questionBoardThumbnailRepository;
    private final AuthenticationService authenticationService;

    /**
     * 게시글 생성
     *
     * @param dto 게시글 요청 DTO
     * @return 생성된 게시글 응답 DTO
     */
    @Override
    public QuestionBoardResponse create(QuestionBoardRequest dto) {
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
                // 썸네일 이미지 저장
                if(dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()){
                    QuestionBoardThumbnail questionBoardThumbnail = questionBoardThumbnailImageService.saveImage(dto.getThumbnail());
                    questionBoard.addThumbnail(questionBoardThumbnail);
                }

                // 이미지 저장
                if(dto.getImages() != null && !dto.getImages().isEmpty()) {
                    List<QuestionBoardImage> questionBoardImages = questionBoardImageService.saveImages(dto.getImages());
                    for (QuestionBoardImage image : questionBoardImages) {
                        questionBoard.addImage(image);
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
            }

            user.addBoard(questionBoard);
            questionBoardRepository.save(questionBoard);

            return QuestionBoardResponse.builder()
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
    public Page<QuestionBoardResponse> boardPage(QuestionBoardSearch dto) {
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
                .map(board -> QuestionBoardResponse.builder()
                        .id(board.getId())
                        .nickname(board.getUser().getActive() ? board.getUser().getNickname() : "탈퇴한 사용자")
                        .title(board.getTitle())
                        .createDate(board.getCreateDate())
                        .commentCount(board.getQuestionBoardComments().size())
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
    public QuestionBoardResponse getDetail(Long boardId) {
        QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        List<String> imagesName = questionBoard.getImages().stream()
                .map(QuestionBoardImage::getFileName)
                .collect(Collectors.toList());
        return QuestionBoardResponse.builder()
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
    public Page<QuestionBoardResponse> getBoardsForUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<QuestionBoard> boards = questionBoardRepository.findByUserId(userId, pageable);

        return boards
                .map(board -> QuestionBoardResponse.builder()
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
     * 게시글 작성자 닉네임 조회
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
     * 내가 쓴 글인지 체크
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
    public QuestionBoardResponse getBoardUpdateData(Long boardId) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if (user.getLoginId().equals(questionBoard.getUser().getLoginId())) {
                List<String> imagesName = questionBoard.getImages().stream()
                        .map(QuestionBoardImage::getOriginalName)
                        .collect(Collectors.toList());
                return QuestionBoardResponse.builder()
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
    public QuestionBoardResponse update(Long boardId, QuestionBoardRequest dto) {
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            if (questionBoard.getUser().getId().equals(customUserDetails.getId())) {
                questionBoard.setTitle(dto.getTitle());
                questionBoard.setContent(dto.getContent());

                // 썸네일 이미지 수정
                if(dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()){
                    List<QuestionBoardThumbnail> questionBoardThumbnail = questionBoardThumbnailRepository.findByQuestionBoardId(boardId);
                    if (!questionBoardThumbnail.isEmpty()) { // 리스트가 비어있지 않은 경우에만 삭제 시도
                        questionBoardThumbnailRepository.delete(questionBoardThumbnail.get(0));
                    }
                    try{
                        QuestionBoardThumbnail questionBoardThumbnail1 = questionBoardThumbnailImageService.saveImage(dto.getThumbnail());
                        questionBoard.addThumbnail(questionBoardThumbnail1);
                    } catch (Exception e){
                        throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
                    }
                }

                List<String> originalImageNames = dto.getOriginalImages();
                List<QuestionBoardImage> dbImages = questionBoardImageRepository.findByQuestionBoardId(boardId);

                // 기존 이미지 처리 로직
                for (QuestionBoardImage questionBoardImage : dbImages) {
                    if(originalImageNames == null || !originalImageNames.contains(questionBoardImage.getOriginalName())){
                        questionBoardImageRepository.delete(questionBoardImage);
                    }
                }

                // 새로운 이미지 저장 로직
                try {
                    if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                        List<QuestionBoardImage> questionBoardImages = questionBoardImageService.saveImages(dto.getImages());
                        for (QuestionBoardImage image : questionBoardImages) {
                            questionBoard.addImage(image);
                        }
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
                }

                questionBoardRepository.save(questionBoard);

                return QuestionBoardResponse.builder()
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

    /**
     * 게시글 삭제
     *
     * @param boardId 게시글 ID
     */
    @Override
    public void delete(Long boardId){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            QuestionBoard questionBoard = questionBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));
            if(customUserDetails.getId().equals(questionBoard.getUser().getId())){
                questionBoardRepository.delete(questionBoard);
            }else{
                throw new IllegalArgumentException("글 작성자만 삭제 가능합니다");
            }
        }else{
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }
}
