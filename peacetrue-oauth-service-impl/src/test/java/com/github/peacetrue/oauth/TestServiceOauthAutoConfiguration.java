package com.github.peacetrue.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.peacetrue.converter.ResultConverter;
import com.github.peacetrue.converter.ResultConverterImpl;
import com.github.peacetrue.result.exception.DataResultException;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiayx
 */
@Configuration
@ImportAutoConfiguration(value = {
        ServiceOauthAutoConfiguration.class,
        JacksonAutoConfiguration.class
})
@PropertySource("classpath:/application-oauth-service-test.properties")
public class TestServiceOauthAutoConfiguration {

    /** 符合京东的格式 */
    @Bean
    @ConditionalOnMissingBean(ResultConverter.class)
    public ResultConverter jdResultConverter(ObjectMapper jacksonObjectMapper) {
        ResultConverterImpl resultConverter = new ResultConverterImpl();
        resultConverter.setChecker(result -> {
            Integer code = (Integer) result.get("code");
            String msg = (String) result.get("msg");
            String requestId = (String) result.get("requestId");
            if (code != null && msg != null && requestId != null) {
                throw new DataResultException(String.valueOf(code), msg, requestId);
            }
        });
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("access_token", "accessToken");
        keyMap.put("expires_in", "expiresIn");
        keyMap.put("refresh_token", "refreshToken");
        keyMap.put("open_id", "openId");
        keyMap.put("time", "createdTime");
        resultConverter.setConverter((result, javaType) -> {
            if (javaType.getRawClass().equals(AccessToken.class)) {
                replaceKey(result, keyMap);
            }
            return jacksonObjectMapper.convertValue(result, javaType);
        });
        return resultConverter;
    }


    public static <K, V> void replaceKey(Map<K, V> source, Map<K, K> keyMap) {
        keyMap.forEach((key, value) -> {
            if (source.containsKey(value)) throw new IllegalStateException(String.format("the key '%s' already exists", value));
            source.put(value, source.get(key));
        });
    }
}
