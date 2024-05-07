package com.example.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/review/create", "/review/edit/*", "/qna/create", "/qna/edit/*").authenticated()
                        .requestMatchers("/", "/main", "/login", "/loginProc",
                                "/join", "/joinProc", "/qna", "/qna/*", "/review", "/review/*",
                                "/checkLoginId", "/checkNickname", "/review/*/comments", "/create/*/comments",
                                "/api/review", "/api/review/*").permitAll()

                        .anyRequest().authenticated());

        http
                .formLogin((auth) -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/", true)
                        .permitAll());

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(3) // 최대 세션 허용 수
                        .maxSessionsPreventsLogin(true) // false = 기존 세션 만료
                );

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId() // 사용자가 인증 성공하면, 기존 사용자의 세션에 Session ID만 변경
                );

        http
                .logout((auth) -> auth
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID", "remember-me")
                );

        return http.build();
    }
}
