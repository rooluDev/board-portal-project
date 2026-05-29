package com.admin.backend.config;

import com.admin.backend.common.interceptor.InterceptorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor Configuration
 */
@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class WebMvcDevConfig implements WebMvcConfigurer {

    private final InterceptorHandler interceptorHandler;

    /**
     * 인터셉터 등록
     * /login 경로를 제외한 모든 경로에 인터셉터를 적용
     *
     * @param registry 인터셉터 레지스트리
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // /login을 제외한 /으로 시작한 모든 uri 인터셉터 설정
        registry.addInterceptor(interceptorHandler)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }

    /**
     * 로컬 파일 시스템 업로드 디렉토리를 정적 리소스 핸들러로 등록
     *
     * @param registry 리소스 핸들러 레지스트리
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
}
