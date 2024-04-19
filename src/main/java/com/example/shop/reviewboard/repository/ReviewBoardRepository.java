package com.example.shop.reviewboard.repository;

import com.example.shop.reviewboard.entity.ReviewBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {
}
