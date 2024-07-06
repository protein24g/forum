package com.example.forum.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/freeBoard/create", "/questionBoard/create").authenticated()
                        .requestMatchers("/", "/mobile/font.css", "/logo/**", "/favicon.ico",
                                "/account/logout.js", "/navbar/active.js",
                                "/login", "/loginProc", "/join", "/joinProc",
                                "/users/*", "/api/users/**",
                                "/checkLoginId", "/checkNickname",
                                "/userinfo/**", "/api/userinfo/**").permitAll()
                        .requestMatchers( // 이미지 업로드 경로
                                "/freeBoardUpload/**", "/profileUpload/**")
                        .permitAll()
                        .requestMatchers( // 자유 게시판
                                "/freeBoard/**", "/api/freeBoard/**", "/api/freeBoard/*/comments"
                        ).permitAll()
                        .requestMatchers( // 질문과 토론 게시판
                                "/questionBoard/**", "/api/questionBoard/**", "/api/questionBoard/*/comments",
                                "/questionBoardUpload/**"
                        ).permitAll()
                        .anyRequest().authenticated());

        http
                .formLogin((auth) -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/")
                        .permitAll());

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId() // 세션 고정 보호
                        .maximumSessions(3) // 최대 세션 허용 수
                        .maxSessionsPreventsLogin(true)); // 동시 로그인 방지

        http
                .logout((auth) -> auth
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID", "remember-me"));
        return http.build();
    }
}
