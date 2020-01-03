package com.github.peacetrue.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ControllerOauthProperties.class)
@ComponentScan(basePackageClasses = ControllerOauthAutoConfiguration.class)
@PropertySource("classpath:/application-oauth-controller.properties")
public class ControllerOauthAutoConfiguration {

    public static final String ATTRIBUTE_HTTP_SESSION_OAUTH_ACCESS_TOKEN = "oauthAccessTokenHttpSession";
    public static final String ATTRIBUTE_HTTP_SESSION_OAUTH_TARGET_URL = "oauthTargetUrlHttpSession";
    public static final String SCHEDULED_EXECUTOR_SERVICE_OAUTH = "oauthScheduledExecutorService";

    private ControllerOauthProperties controllerOauthProperties;

    public ControllerOauthAutoConfiguration(ControllerOauthProperties controllerOauthProperties) {
        this.controllerOauthProperties = controllerOauthProperties;
    }

    @Bean(name = ATTRIBUTE_HTTP_SESSION_OAUTH_ACCESS_TOKEN)
    @ConditionalOnMissingBean(name = ATTRIBUTE_HTTP_SESSION_OAUTH_ACCESS_TOKEN)
    public AttributeHttpSession oauthAccessTokenHttpSession() {
        return new AttributeHttpSessionImpl("oauth.accessToken");
    }

    @Bean(name = ATTRIBUTE_HTTP_SESSION_OAUTH_TARGET_URL)
    @ConditionalOnMissingBean(name = ATTRIBUTE_HTTP_SESSION_OAUTH_TARGET_URL)
    public AttributeHttpSession oauthTargetUrlHttpSession() {
        return new AttributeHttpSessionImpl("oauth.targetUrl");
    }

    @Bean
    public AuthorizationHandler authorizationHandler() {
        return new AuthorizationHandler();
    }

    @Bean
    public AuthorizationCallbackOauthController authorizationCallbackOauthController() {
        return new AuthorizationCallbackOauthController();
    }

    @Bean(name = SCHEDULED_EXECUTOR_SERVICE_OAUTH)
    @ConditionalOnProperty(prefix = "peacetrue.oauth", name = "enableAutoRefresh", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(name = SCHEDULED_EXECUTOR_SERVICE_OAUTH, value = ScheduledExecutorService.class)
    public ScheduledExecutorService oauthScheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(10);
    }


    public static class TraditionModeConditions extends AllNestedConditions {
        public TraditionModeConditions() {
            super(ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty(prefix = "peacetrue.oauth", name = "traditionMode", havingValue = "true", matchIfMissing = true)
        public static class EnableTraditionMode {}

        @ConditionalOnProperty(prefix = "peacetrue.oauth", name = "securedUrls")
        public static class HavingSecuredUrls {}
    }

    @Configuration
    @Conditional(TraditionModeConditions.class)
    public static class TraditionModeAutoConfiguration {

        @Value("${peacetrue.oauth.secured-urls}")
        private String[] securedUrls;

        @Bean
        public WebMvcConfigurer oauthTraditionWebMvcConfigurer() {
            return new WebMvcConfigurerAdapter() {
                @Override
                public void addInterceptors(InterceptorRegistry registry) {
                    registry.addInterceptor(oauthHandlerInterceptor()).addPathPatterns(Objects.requireNonNull(securedUrls));
                }
            };
        }

        @Bean
        public OauthHandlerInterceptor oauthHandlerInterceptor() {
            return new OauthHandlerInterceptor();
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "peacetrue.oauth", name = "traditionMode", havingValue = "false")
    public static class FrontModeAutoConfiguration {
        @Bean
        public AuthorizationCheckOauthController authorizationCheckOauthController() {
            return new AuthorizationCheckOauthController();
        }
    }

}
