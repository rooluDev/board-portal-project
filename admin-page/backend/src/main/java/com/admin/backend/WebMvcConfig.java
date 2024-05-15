package com.admin.backend;

import com.admin.backend.common.interceptor.InterceptorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor Configuration
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final InterceptorHandler interceptorHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // /login을 제외한 /으로 시작한 모든 uri 인터셉터 설정
        registry.addInterceptor(interceptorHandler)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }

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
