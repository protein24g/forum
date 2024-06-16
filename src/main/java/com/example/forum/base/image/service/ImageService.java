package com.example.forum.base.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    /**
     * 파일의 확장자를 추출하여 반환
     *
     * @param fileName 파일 이름
     * @return 파일 확장자
     */
    public String getFileExtension(String fileName) {
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
    public boolean isValidExtension(String extension) {
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
