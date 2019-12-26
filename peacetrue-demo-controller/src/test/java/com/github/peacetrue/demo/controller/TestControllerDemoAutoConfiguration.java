package com.github.peacetrue.demo.controller;

import com.github.peacetrue.demo.service.TestServiceDemoAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author xiayx
 */
@Configuration
@ImportAutoConfiguration(classes = {
        TestServiceDemoAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        ControllerDemoAutoConfiguration.class,
})
@PropertySource("classpath:application-demo-controller-test.properties")
public class TestControllerDemoAutoConfiguration {
}
