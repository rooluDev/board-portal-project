package com.user.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * Properties Dev Config
 */
@Configuration
@Profile("dev")
public class PropertiesDevConfig {

    /**
     * JWT 설정: @Value로 환경변수를 직접 주입하여 Properties 구성
     * (PropertiesFactoryBean은 ${...} 환경변수를 해석하지 않으므로 직접 주입)
     */
    @Bean(name = "jwt")
    public Properties jwtProperties(@Value("${JWT_SECRET}") String jwtSecret) {
        Properties props = new Properties();
        props.setProperty("secret", jwtSecret);
        props.setProperty("headerKey", "Authorization");
        return props;
    }

    /**
     * 스토리지 설정: 로컬 경로를 환경변수에서 주입
     */
    @Bean(name = "storage")
    public Properties storageProperties(@Value("${STORAGE_PATH}") String storagePath) {
        Properties props = new Properties();
        props.setProperty("path", storagePath);
        return props;
    }

    /**
     * 파일 제약조건 설정: constraint.properties 파일을 로드하여 빈으로 등록
     *
     * @return constraint.properties 를 담은 PropertiesFactoryBean
     */
    @Bean(name = "constraint")
    public PropertiesFactoryBean constraintPropertiesBean() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("properties/constraint.properties");

        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }
}
