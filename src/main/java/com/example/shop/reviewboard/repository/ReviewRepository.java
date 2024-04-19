package com.example.shop.reviewboard.repository;

import com.example.shop.reviewboard.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
