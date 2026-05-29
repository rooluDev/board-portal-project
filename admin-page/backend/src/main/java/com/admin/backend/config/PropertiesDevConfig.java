package com.admin.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * 개발(dev) 환경용 프로퍼티 설정 클래스
 */
@Configuration
@Profile("dev")
public class PropertiesDevConfig {

    /**
     * 스토리지 설정: 로컬 경로를 환경변수에서 주입
     * (PropertiesFactoryBean은 ${...} 환경변수를 해석하지 않으므로 직접 주입)
     */
    @Bean(name = "storage")
    public Properties storageProperties(@Value("${STORAGE_PATH}") String storagePath) {
        Properties props = new Properties();
        props.setProperty("path", storagePath);
        return props;
    }

    /**
     * 파일 업로드 제약조건 프로퍼티 빈 등록
     * constraint.properties 파일을 로드하여 빈으로 등록
     *
     * @return constraint.properties를 기반으로 생성된 PropertiesFactoryBean
     */
    @Bean(name = "constraint")
    public PropertiesFactoryBean constraintPropertiesBean(){
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("properties/constraint.properties");

        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }

}
