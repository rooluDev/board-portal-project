package com.user.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapperConfig
 */
@Configuration
public class ModelMapperConfig {

    /**
     * ModelMapper 빈 등록
     *
     * @return ModelMapper 인스턴스
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
