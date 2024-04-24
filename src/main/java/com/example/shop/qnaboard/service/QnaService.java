package com.example.shop.qnaboard.service;

import com.example.shop.comment.dto.response.CommentResponse;
import com.example.shop.comment.entity.Comment;
import com.example.shop.qnaboard.dto.requests.QnaRequest;
import com.example.shop.qnaboard.dto.response.QnaResponse;
import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.qnaboard.repository.QnaRepository;
import com.example.shop.user.dto.CustomUserDetails;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;

    // C(Create)
    public QnaResponse Create(QnaRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

            QuestionAndAnswer questionAndAnswer = QuestionAndAnswer.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .visibility(dto.isVisibility())
                    .build();
            user.addQuestionAndAnswer(questionAndAnswer);
            qnaRepository.save(questionAndAnswer);

            return QnaResponse.builder()
                    .id(questionAndAnswer.getId())
                    .nickname(questionAndAnswer.getUser().getNickname())
                    .title(questionAndAnswer.getTitle())
                    .content(questionAndAnswer.getContent())
                    .createDate(questionAndAnswer.getCreateDate())
                    .build();
        }else{
            throw new IllegalArgumentException("로그인 후 이용 가능합니다.");
        }
    }

    // R(Read)
    public Page<QnaResponse> Page(String keyword, int page, String option) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<QuestionAndAnswer> questionAndAnswers = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색어가 있는 경우
            switch (option){
                case "1":
                    questionAndAnswers = qnaRepository.findByTitleContaining(keyword, pageable);
                    break;
                case "2":
                    questionAndAnswers = qnaRepository.findByContentContaining(keyword, pageable);
                    break;
            }

        } else {
            // 검색어가 없는 경우
            questionAndAnswers = qnaRepository.findAll(pageable);
        }

        return questionAndAnswers
                .map(questionAndAnswer -> QnaResponse.builder()
                    .id(questionAndAnswer.getId())
                    .nickname(questionAndAnswer.getUser().getNickname())
                    .title(questionAndAnswer.getTitle())
                    .content(questionAndAnswer.getContent())
                    .createDate(questionAndAnswer.getCreateDate())
                    .visibility(questionAndAnswer.isVisibility())
                    .completed(questionAndAnswer.isCompleted())
                    .build());
    }

    public QnaResponse ReadDetail(Long boardNum) {
        QuestionAndAnswer questionAndAnswer = qnaRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if(questionAndAnswer.isVisibility()){ // 비공개 true, 공개 false
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication.getPrincipal() instanceof CustomUserDetails){ // 로그인 확인
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

                // 유저
                User user = userRepository.findById(customUserDetails.getId()) // 로그인 된 유저 객체
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

                if(user.getRole().equals(User.Role.USER)){ // 로그인 된 유저 권한이 USER 라면
                    if(!customUserDetails.getLoginId().equals(questionAndAnswer.getUser().getLoginId())){ // 작성자 id와 로그인 된 유저 id 비교
                        throw new IllegalArgumentException("본인 게시글만 읽기 가능합니다.");
                    }
                }
            }else{
                throw new IllegalArgumentException("로그인 후 이용하세요.");
            }
        }
        // 댓글 목록 가져오기
        List<Comment> comments = questionAndAnswer.getComments();
        // Comment 객체를 CommentResponse 로 변환
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> CommentResponse.builder()
                        .nickname(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createDate(comment.getCreateDate())
                        .build())
                .collect(Collectors.toList());

        return QnaResponse.builder()
                .id(questionAndAnswer.getId())
                .nickname(questionAndAnswer.getUser().getNickname())
                .title(questionAndAnswer.getTitle())
                .content(questionAndAnswer.getContent())
                .createDate(questionAndAnswer.getCreateDate())
                .commentResponses(commentResponses)
                .completed(questionAndAnswer.isCompleted())
                .build();
    }
}
