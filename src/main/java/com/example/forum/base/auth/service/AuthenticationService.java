package com.example.forum.base.auth.service;

import com.example.forum.user.auth.dto.requests.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 인증 관련 서비스
 */
@Service
public class AuthenticationService {

    /**
     * 현재 인증된 사용자 가져오기
     *
     * @return CustomUserDetails 객체 또는 null
     */
    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 인증 여부 확인
     *
     * @return 인증된 경우 true, 아니면 false
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * 관리자 권한 확인
     *
     * @return 관리자 권한이 있는 경우 true, 아니면 false
     */
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}
