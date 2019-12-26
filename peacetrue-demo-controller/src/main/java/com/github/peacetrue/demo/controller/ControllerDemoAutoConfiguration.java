package com.github.peacetrue.demo.controller;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ControllerDemoProperties.class)
@PropertySource("classpath:/application-demo-controller.properties")
public class ControllerDemoAutoConfiguration {

    @Bean
    public DemoController demoController() {
        return new DemoController();
    }
}
