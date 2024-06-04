package com.admin.backend.config;

import com.admin.backend.common.interceptor.InterceptorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class WebMvcProdConfig implements WebMvcConfigurer {

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
                .addResourceLocations("file:///home/ubuntu/upload/free/");

        registry.addResourceHandler("/upload/gallery/**")
                .addResourceLocations("file:///home/ubuntu/upload/gallery/");

        registry.addResourceHandler("/upload/thumbnail/**")
                .addResourceLocations("file:///home/ubuntu/upload/thumbnail/");
    }
}
