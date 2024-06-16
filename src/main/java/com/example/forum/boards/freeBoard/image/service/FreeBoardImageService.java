package com.example.forum.boards.freeBoard.image.service;

import com.example.forum.boards.freeBoard.image.entity.FreeBoardImage;
import com.example.forum.boards.freeBoard.image.repository.FreeBoardImageRepository;
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
public class FreeBoardImageService {
    private final FreeBoardImageRepository freeBoardImageRepository;

    @Value("${image.dir}")
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
    public List<FreeBoardImage> saveImages(List<MultipartFile> files) throws Exception {
        List<FreeBoardImage> savedImages = new ArrayList<>();

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
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!isValidExtension(fileExtension)) {
                throw new IllegalArgumentException("유효하지 않은 파일 확장자입니다");
            }

            // 파일 저장
            String fileName = UUID.randomUUID() + fileExtension;
            Path filePath = Paths.get(uploadDir, fileName); // 파일 저장 위치
            Files.copy(file.getInputStream(), filePath);

            // 이미지 정보 저장
            FreeBoardImage savedImage = freeBoardImageRepository.save(
                    FreeBoardImage.builder()
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

    /**
     * 파일의 확장자를 추출하여 반환
     *
     * @param fileName 파일 이름
     * @return 파일 확장자
     */
    private String getFileExtension(String fileName) {
        // 파일 이름이 null인지, 비어 있는지, 또는 점('.')을 포함하고 있는지 확인
        if (fileName == null || fileName.isEmpty() || !fileName.contains(".")) {
            return "";
        }
        // fileName.lastIndexOf('.'): 파일 이름에서 마지막 점('.')의 인덱스를 찾습니다.
        // fileName.substring(...): 인덱스부터 마지막 인덱스까지 문자열 반환
        return fileName.substring(fileName.lastIndexOf('.')); // "."의 인덱스부터 끝까지 문자열을 반환
    }

    /**
     * 파일 확장자가 유효성 검사
     *
     * @param extension 파일 확장자
     * @return 유효한 확장자인 경우 true, 그렇지 않으면 false
     */
    private boolean isValidExtension(String extension) {
        // 유효한 파일 확장자들
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".gif"};
        for (String validExtension : validExtensions) {
            if (extension.equalsIgnoreCase(validExtension)) { // equalsIgnoreCase: 대소문자 구분 X
                return true;
            }
        }
        return false;
    }
}