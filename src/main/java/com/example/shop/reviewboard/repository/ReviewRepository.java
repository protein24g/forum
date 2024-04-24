package com.example.shop.reviewboard.repository;

import com.example.shop.reviewboard.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByTitleContaining(String keyword, Pageable pageable);
    Page<Review> findByContentContaining(String keyword, Pageable pageable);
}
