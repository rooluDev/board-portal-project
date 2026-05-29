package com.user.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc Prod Config
 */
@Configuration
@Profile("prod")
public class WebMvcProdConfig implements WebMvcConfigurer {

    /**
     * CORS 설정: 운영 환경에서 허용된 오리진만 허용
     *
     * @param registry CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://xn--4k0bj36a8obp7hdvo59brzcrug.site",
                        "https://www.xn--4k0bj36a8obp7hdvo59brzcrug.site"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
