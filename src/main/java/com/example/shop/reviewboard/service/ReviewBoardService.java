package com.example.shop.reviewboard.service;

import com.example.shop.reviewboard.repository.ReviewBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewBoardService {
    private final ReviewBoardRepository reviewBoardRepository;
}
