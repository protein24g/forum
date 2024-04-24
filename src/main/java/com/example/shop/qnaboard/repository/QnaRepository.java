package com.example.shop.qnaboard.repository;

import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QnaRepository extends JpaRepository<QuestionAndAnswer, Long> {
    Page<QuestionAndAnswer> findAll(Pageable pageable);

    Page<QuestionAndAnswer> findByTitleContaining(String keyword, Pageable pageable);
    Page<QuestionAndAnswer> findByContentContaining(String keyword, Pageable pageable);

    @Override
    Optional<QuestionAndAnswer> findById(Long aLong);
}
