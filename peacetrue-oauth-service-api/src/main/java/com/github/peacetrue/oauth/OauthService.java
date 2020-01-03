package com.github.peacetrue.oauth;

/**
 * @author xiayx
 */
public interface OauthService {

    /**
     * 生成授权链接
     *
     * @param scope 作用域
     * @return 授权链接
     */
    String generateAuthorizationUrl(String scope);


    /**
     * 获取访问令牌
     *
     * @param code 认证码
     * @return 访问令牌
     */
    AccessToken getAccessToken(String code);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌值
     * @return 访问令牌
     */
    AccessToken refreshAccessToken(String refreshToken);

}
