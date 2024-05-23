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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/free/**")
                .addResourceLocations("file:///Users/user/upload/free/");

        registry.addResourceHandler("/upload/gallery/**")
                .addResourceLocations("file:///Users/user/upload/gallery/");

        registry.addResourceHandler("/upload/thumbnail/**")
                .addResourceLocations("file:///Users/user/upload/thumbnail/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT")
                .maxAge(3000);
    }
}
