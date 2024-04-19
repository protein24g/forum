package com.example.shop.qnaboard.repository;

import com.example.shop.qnaboard.entity.QnABoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnABoard, Long> {
}
