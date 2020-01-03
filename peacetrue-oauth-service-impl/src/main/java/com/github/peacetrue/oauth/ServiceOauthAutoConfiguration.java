package com.github.peacetrue.oauth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ServiceOauthProperties.class)
@ComponentScan(basePackageClasses = ServiceOauthAutoConfiguration.class)
@PropertySource("classpath:/application-oauth-service.properties")
public class ServiceOauthAutoConfiguration {

    public static final String OAUTH_REST_TEMPLATE = "oauthRestTemplate";

    @Bean(name = OAUTH_REST_TEMPLATE)
    @ConditionalOnMissingBean(name = OAUTH_REST_TEMPLATE)
    public RestTemplate oauthRestTemplate() {
        return new RestTemplate();
    }


}
