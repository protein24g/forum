package com.example.forum.boards.questionBoard.image.service;

import com.example.forum.base.image.service.ImageService;
import com.example.forum.boards.questionBoard.image.entity.QuestionBoardImage;
import com.example.forum.boards.questionBoard.image.repository.QuestionBoardImageRepository;
import com.sun.nio.sctp.IllegalReceiveException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 이미지 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class QuestionBoardImageService {
    private final QuestionBoardImageRepository questionBoardImageRepository;
    private final ImageService imageService;

    @Value("${questionBoardImage.dir}")
    private String uploadDir;

    // 파일 업로드 최대 크기 설정 (예: 5MB)
    private final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    /**
     * 여러 이미지를 저장하고 저장된 이미지 목록을 반환
     *
     * @param files 이미지 파일 목록
     * @return 저장된 이미지 목록
     * @throws Exception 파일 처리 중 예외가 발생할 경우
     */
    public List<QuestionBoardImage> saveImages(List<MultipartFile> files) throws Exception {
        List<QuestionBoardImage> savedImages = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            throw new IllegalReceiveException("파일이 존재하지 않습니다");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 빈 파일은 무시
            }

            // 파일 크기 확인
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("파일 크기는 5MB를 넘을 수 없습니다");
            }

            // 파일 확장자 확인
            String fileExtension = imageService.getFileExtension(file.getOriginalFilename());
            if (!imageService.isValidExtension(fileExtension)) {
                throw new IllegalArgumentException("유효하지 않은 파일 확장자입니다");
            }

            // 파일 저장
            String fileName = UUID.randomUUID() + fileExtension;
            Path filePath = Paths.get(uploadDir, fileName); // 파일 저장 위치
            Files.copy(file.getInputStream(), filePath);

            // 이미지 정보 저장
            QuestionBoardImage savedImage = questionBoardImageRepository.save(
                    QuestionBoardImage.builder()
                            .originalName(file.getOriginalFilename())
                            .fileName(fileName)
                            .filePath(filePath.toString())
                            .createDate(LocalDateTime.now())
                            .build()
            );
            savedImages.add(savedImage);
        }
        return savedImages;
    }
}