package com.user.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc Dev Config
 */
@Configuration
@Profile("dev")
public class WebMvcDevConfig implements WebMvcConfigurer {

    /**
     * 로컬 업로드 파일(자유게시판, 갤러리, 썸네일)에 대한 정적 리소스 핸들러 등록
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/free/**")
                .addResourceLocations("file:///Users/user/upload/free/");

        registry.addResourceHandler("/upload/gallery/**")
                .addResourceLocations("file:///Users/user/upload/gallery/");

        registry.addResourceHandler("/upload/thumbnail/**")
                .addResourceLocations("file:///Users/user/upload/thumbnail/");
    }

    /**
     * CORS 설정: 개발 환경에서 모든 오리진 허용
     *
     * @param registry CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT")
                .maxAge(3000);
    }
}
