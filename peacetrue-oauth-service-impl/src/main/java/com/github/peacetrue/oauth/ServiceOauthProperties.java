package com.github.peacetrue.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 服务层配置
 *
 * @author xiayx
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "peacetrue.oauth")
public class ServiceOauthProperties {

    /** 应用标识 */
    private String appKey;
    /** 应用秘钥 */
    private String appSecret;
    /** 授权地址 */
    private String authorizationUrl;
    /** 作用域的值 */
    private String scopeValue;
    /** 授权通过后的回调地址 */
    private String callbackUrl;
    /** 获取访问令牌的地址 */
    private String accessTokenUrl;
    /** 刷新访问令牌的地址 */
    private String refreshTokenUrl;

}