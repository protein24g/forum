    package com.example.shop.user.dto;

    import com.example.shop.user.entity.User;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.Collections;

    @RequiredArgsConstructor
    public class CustomUserDetails implements UserDetails {
        private final User user;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        }

        public Long getId(){ return user.getId(); }

        public String getLoginId() {
            return user.getLoginId();
        }

        @Override
        public String getPassword() {
            return user.getLoginPw();
        }

        @Override
        public String getUsername() {
            return user.getNickname();
        }

        @Override
        public boolean isAccountNonExpired() {
            // 계정 만료 여부를 반환합니다. 여기서는 단순화를 위해 true를 반환합니다.
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            // 계정 잠김 여부를 반환합니다. 여기서는 단순화를 위해 true를 반환합니다.
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            // 자격 증명 만료 여부를 반환합니다. 여기서는 단순화를 위해 true를 반환합니다.
            return true;
        }

        @Override
        public boolean isEnabled() {
            // 계정 활성화 여부를 반환합니다. 실제로는 Member 객체의 상태에 따라 결정되어야 합니다.
            return user.getActive();
        }
    }
