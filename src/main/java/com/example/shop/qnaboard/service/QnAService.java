package com.example.shop.qnaboard.service;

import com.example.shop.qnaboard.repository.QnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnAService {
    private final QnARepository qnARepository;

}
