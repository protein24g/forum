package com.example.forum.user.auth.dto.requests;

import com.example.forum.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 사용자의 세부 정보를 나타내는 클래스
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    /**
     * 사용자의 권한 정보를 반환
     * @return 사용자의 권한 정보
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    /**
     * 사용자의 ID를 반환
     * @return 사용자의 ID
     */
    public Long getId(){ return user.getId(); }

    /**
     * 사용자의 로그인 ID를 반환
     * @return 사용자의 로그인 ID
     */
    public String getLoginId() {
        return user.getLoginId();
    }

    /**
     * 사용자의 비밀번호를 반환
     * @return 사용자의 비밀번호
     */
    @Override
    public String getPassword() {
        return user.getLoginPw();
    }

    /**
     * 사용자의 닉네임을 반환
     * @return 사용자의 닉네임
     */
    @Override
    public String getUsername() {
        return user.getNickname();
    }

    /**
     * 계정이 만료되었는지 여부 반환
     * @return 계정 만료 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겼는지 여부 반환
     * @return 계정 잠금 여부
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명이 만료되었는지 여부 반환
     * @return 자격 증명 만료 여부
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화되었는지 여부 반환
     * @return 계정 활성화 여부
     */
    @Override
    public boolean isEnabled() {
        return user.getActive();
    }
}
