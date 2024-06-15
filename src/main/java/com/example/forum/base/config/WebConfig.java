package com.example.forum.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // 정적 리소스 핸들러를 추가하는 메서드. 이 메서드를 통해 특정 URL 패턴에 해당하는 요청에 대한 정적 자원을 제공
        registry.addResourceHandler("/freeBoardUpload/**", "/questionBoardUpload/**") // /?로 시작하는 모든 요청에 대해 정적 리소스 핸들러를 설정
                .addResourceLocations("file:/c:/projects/imageUpload/"); // 실제 이미지가 저장된 경로로 수정
    }
}
