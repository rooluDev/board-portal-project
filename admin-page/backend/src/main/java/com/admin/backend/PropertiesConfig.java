package com.admin.backend;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PropertiesConfig {

    @Bean(name = "storage")
    public PropertiesFactoryBean filePropertiesBean() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("properties/storage.properties");

        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }

    @Bean(name = "constraint")
    public PropertiesFactoryBean constraintPropertiesBean(){
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("properties/constraint.properties");

        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }

}
