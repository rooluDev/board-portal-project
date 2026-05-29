package com.admin.backend.config;

import com.admin.backend.common.interceptor.InterceptorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 운영(prod) 환경용 Spring MVC 설정 클래스
 */
@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class WebMvcProdConfig implements WebMvcConfigurer {

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
}
