package com.example.forum.boards.freeBoard.image.service;

import com.example.forum.base.image.service.ImageService;
import com.example.forum.boards.freeBoard.image.entity.FreeBoardImage;
import com.example.forum.boards.freeBoard.image.entity.FreeBoardThumbnail;
import com.example.forum.boards.freeBoard.image.repository.FreeBoardImageRepository;
import com.example.forum.boards.freeBoard.image.repository.FreeBoardThumbnailRepository;
import com.sun.nio.sctp.IllegalReceiveException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 이미지 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FreeBoardThumbnailImageService {
    private final FreeBoardThumbnailRepository freeBoardThumbnailRepository;
    private final ImageService imageService;

    @Value("${image.dir}")
    private String uploadDir;

    // 파일 업로드 최대 크기 설정 (예: 5MB)
    private final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 썸네일 이미지를 저장하고 저장된 이미지 반환
     *
     * @param file 이미지 파일
     * @return 저장된 이미지
     * @throws Exception 파일 처리 중 예외가 발생할 경우
     */
    public FreeBoardThumbnail saveImage(MultipartFile file) throws Exception{
        if (file == null || file.isEmpty()) {
            throw new IllegalReceiveException("파일이 존재하지 않습니다");
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
        FreeBoardThumbnail savedImage = freeBoardThumbnailRepository.save(
                FreeBoardThumbnail.builder()
                        .originalName(file.getOriginalFilename())
                        .fileName(fileName)
                        .filePath(filePath.toString())
                        .createDate(LocalDateTime.now())
                        .build()
        );

        return savedImage;
    }
}