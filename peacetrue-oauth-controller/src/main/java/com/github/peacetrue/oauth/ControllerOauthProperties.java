package com.github.peacetrue.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;

/**
 * 控制层配置
 *
 * @author xiayx
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "peacetrue.oauth")
public class ControllerOauthProperties {

    /** 授权后的回调地址，默认空字符串，参考 {@link AuthorizationCallbackOauthController#handleAuthorizationCallback} */
    private String authorizationCallbackUrl = "";
    /** 是否传统模式，否则前后端分离模式 */
    private Boolean traditionMode = true;
    /** 需要授权的地址，配置规则参考 {@link AntPathMatcher} */
    private String[] securedUrls;
    /** 授权检查地址，检查当前用户是否已授权 */
    private String authorizationCheckUrl = "/oauth/authorization/check";
    /** 启用自动刷新访问令牌 */
    private Boolean enableAutoRefresh = true;
    /** 用于生成【授权地址】的地址 */
    private String url4GenerateAuthorizationUrl = "/oauth/authorization/url";
    /**
     * 提前刷新时间，单位毫秒，此参数可用于调整本地应用和授权应用时间差值
     *
     * @see AuthorizationCallbackOauthController#prerefreshTime 默认提前一分钟
     */
    private Integer prerefreshTime;

}