package com.example.shop.qnaboard.service;

import com.example.shop.qnaboard.dto.requests.QnaRequest;
import com.example.shop.qnaboard.dto.response.QnaResponse;
import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.qnaboard.repository.QnaRepository;
import com.example.shop.user.dto.CustomUserDetails;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
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
public class QnaService {
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;

    // C(Create)
    public QnaResponse create(QnaRequest dto) {
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
    public Page<QnaResponse> page(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        return qnaRepository.findAll(pageable)
                .map(questionAndAnswer -> QnaResponse.builder()
                        .id(questionAndAnswer.getId())
                        .nickname(questionAndAnswer.getUser().getNickname())
                        .title(questionAndAnswer.getTitle())
                        .content(questionAndAnswer.getContent())
                        .createDate(questionAndAnswer.getCreateDate())
                        .build());
    }

    public QnaResponse readDetail(Long boardNum, Long userId) {
        QuestionAndAnswer questionAndAnswer = qnaRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        if(questionAndAnswer.getUser().getId().equals(userId)){
            return QnaResponse.builder()
                    .id(questionAndAnswer.getId())
                    .nickname(questionAndAnswer.getUser().getNickname())
                    .title(questionAndAnswer.getTitle())
                    .content(questionAndAnswer.getContent())
                    .createDate(questionAndAnswer.getCreateDate())
                    .build();
        }else {
            throw new IllegalArgumentException("본인이 작성한 글만 읽기 가능합니다.");
        }
    }
}
