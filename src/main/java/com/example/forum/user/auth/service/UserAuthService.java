package com.example.forum.user.auth.service;

import com.example.forum.user.auth.dto.requests.JoinRequest;
import com.example.forum.user.auth.repository.UserAuthRepository;
import com.example.forum.user.entity.User;
import com.example.forum.user.profile.profileImage.entity.UserImage;
import com.example.forum.user.profile.profileImage.service.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserImageService userImageService;

    /**
     * 회원 가입
     *
     * @param dto 회원 가입 요청 DTO
     */
    @Transactional
    public void join(JoinRequest dto) {
        if(userAuthRepository.existsByLoginId(dto.getLoginId())){
            throw new IllegalArgumentException("이미 사용중인 아이디 입니다.");
        } else {
            if(userAuthRepository.existsByNickname(dto.getNickname())){
                throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");
            } else {
                User user = userAuthRepository.save(User.builder()
                        .nickname(dto.getNickname())
                        .loginId(dto.getLoginId())
                        .loginPw(passwordEncoder.encode(dto.getLoginPw()))
                        .createDate(LocalDateTime.now())
                        .certificate(dto.getCertificate())
                        .career(dto.getCareer())
                        .role(User.Role.USER)
                        .isActive(true)
                        .build());
                try{
                    // 이미지 저장
                    System.out.println("이밎 ㅣ체크");
                    System.out.println("이미지 들어왔따");
                    System.out.println(dto.getProfile());
                    System.out.println(dto.getProfile().getOriginalFilename());
                    if(dto.getProfile() != null && !dto.getProfile().isEmpty()) {
                        UserImage userImage = userImageService.saveImage(dto.getProfile());
                        System.out.println("userImage 엔티티다" + userImage);
                        user.setUserImage(userImage);
                    }
                } catch (Exception e){
                    throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 로그인 아이디 존재 여부
     *
     * @param loginId 로그인 아이디
     * @return 존재 여부
     */
    public boolean existsByLoginId(String loginId) {
        return userAuthRepository.existsByLoginId(loginId);
    }

    /**
     * 닉네임의 존재 여부
     *
     * @param nickname 닉네임
     * @return 존재 여부
     */
    public boolean existsByNickname(String nickname) {
        return userAuthRepository.existsByNickname(nickname);
    }
}
