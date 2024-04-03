package com.admin.backend;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PropertiesConfig {

    @Bean(name = "file")
    public PropertiesFactoryBean filePropertiesBean() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("properties/file.properties");

        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }

}
