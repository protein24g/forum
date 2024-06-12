package com.example.forum.base.image.service;

import com.example.forum.base.image.entity.Image;
import com.example.forum.base.image.repository.ImageRepository;
import com.sun.nio.sctp.IllegalReceiveException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;

    @Value("${freeBoard.dir}")
    private String uploadDir;

    // 파일 업로드 최대 크기 설정 (예: 5MB)
    private final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    // C(Create)
    // C(Create)
    public List<Image> saveImage(List<MultipartFile> files) throws Exception {
        List<Image> savedImages = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            throw new IllegalReceiveException("파일이 존재하지 않습니다");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 빈 파일은 무시
            }

            System.out.println("파일명 : " + file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.')));

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
            Image savedImage = imageRepository.save(
                    Image.builder()
                            .originalName(file.getOriginalFilename())
                            .fileName(fileName)
                            .filePath(filePath.toString())
                            .build()
            );
            savedImages.add(savedImage);
        }

        return savedImages;
    }


    // 파일 확장자 추출
    private String getFileExtension(String fileName) {
        // 파일 이름이 null인지, 비어 있는지, 또는 점('.')을 포함하고 있는지 확인
        if (fileName == null || fileName.isEmpty() || !fileName.contains(".")) {
            return "";
        }
        // fileName.lastIndexOf('.'): 파일 이름에서 마지막 점('.')의 인덱스를 찾습니다.
        // fileName.substring(...): 인덱스부터 마지막 인덱스까지 문자열 반환
        return fileName.substring(fileName.lastIndexOf('.')); // "."의 인덱스부터 끝까지 문자열을 반환
    }

    // 파일 확장자 확인
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
