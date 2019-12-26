package com.github.peacetrue.demo.service;

import com.github.peacetrue.associate.AssociatedSourceBuilder;
import com.github.peacetrue.associate.AssociatedSourceBuilderImpl;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Objects;

/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ServiceDemoProperties.class)
@MapperScan(basePackageClasses = ServiceDemoAutoConfiguration.class, annotationClass = Mapper.class)
@PropertySource("classpath:/application-demo-service.properties")
public class ServiceDemoAutoConfiguration {

    private ServiceDemoProperties properties;

    public ServiceDemoAutoConfiguration(ServiceDemoProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

    @Bean
    public AssociatedSourceBuilder associatedSourceBuilder() {
        return new AssociatedSourceBuilderImpl();
    }

    @Bean
    public DemoService demoService() {
        return new DemoServiceImpl();
    }

}
