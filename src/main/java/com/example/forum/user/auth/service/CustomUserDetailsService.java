package com.example.forum.user.auth.service;

import com.example.forum.user.auth.dto.requests.CustomUserDetails;
import com.example.forum.user.auth.repository.UserAuthRepository;
import com.example.forum.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자의 상세 정보를 로드하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAuthRepository userAuthRepository;

    /**
     * 주어진 로그인 ID를 기반으로 사용자의 상세 정보 로드
     * @param loginId 사용자의 로그인 ID
     * @return 사용자의 상세 정보
     * @throws UsernameNotFoundException 지정된 로그인 ID를 가진 사용자를 찾을 수 없는 경우 발생하는 예외
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userAuthRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return new CustomUserDetails(user);
    }
}
