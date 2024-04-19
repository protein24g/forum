package com.example.shop.qnaboard.repository;

import com.example.shop.qnaboard.entity.QnA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long> {
}
