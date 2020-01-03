package com.github.peacetrue.oauth;

import com.github.peacetrue.converter.ResultConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiayx
 */
@Service
public class OauthServiceImpl implements OauthService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${peacetrue.oauth.appKey}")
    private String appKey;
    @Value("${peacetrue.oauth.appSecret}")
    private String appSecret;
    @Value("${peacetrue.oauth.authorizationUrl}")
    private String authorizationUrl;
    @Value("${peacetrue.oauth.callbackUrl}")
    private String callbackUrl;
    @Value("${peacetrue.oauth.accessTokenUrl}")
    private String accessTokenUrl;
    @Value("${peacetrue.oauth.refreshTokenUrl}")
    private String refreshTokenUrl;
    @Autowired
    private RestTemplate oauthRestTemplate;
    @Autowired
    private ResultConverter resultConverter;

    @Override
    public String generateAuthorizationUrl(String scope) {
        logger.info("根据作用域[{}]生成【授权链接】", scope);
        return String.format("%s?app_key=%s&response_type=code&scope=%s&redirect_uri=%s",
                authorizationUrl, appKey, scope, getEncode(callbackUrl));
    }

    private String getEncode(String charset) {
        try {
            return URLEncoder.encode(charset, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public AccessToken getAccessToken(String code) {
        logger.info("根据认证码[{}]获取访问令牌", code);
        String url = String.format("%s?app_key=%s&app_secret=%s&grant_type=authorization_code&code=%s",
                accessTokenUrl, appKey, appSecret, code);
        Date createdTime = new Date();
        Map accessTokenMap = oauthRestTemplate.getForObject(url, HashMap.class);
        logger.debug("根据认证码[{}]取得访问令牌[{}]", code, accessTokenMap);
        AccessToken accessToken = resultConverter.convert(accessTokenMap, AccessToken.class);
        accessToken.setCreatedTime(createdTime);
        return accessToken;
    }

    @Override
    public AccessToken refreshAccessToken(String refreshToken) {
        logger.info("使用刷新令牌[{}]刷新访问令牌", refreshToken);
        String url = String.format("%s?app_key=%s&app_secret=%s&grant_type=refresh_token&refresh_token=%s",
                refreshTokenUrl, appKey, appSecret, refreshToken);
        Date createdTime = new Date();
        Map accessTokenMap = oauthRestTemplate.getForObject(url, HashMap.class);
        logger.debug("根据刷新令牌[{}]取得访问令牌[{}]", refreshToken, accessTokenMap);
        AccessToken accessToken = resultConverter.convert(accessTokenMap, AccessToken.class);
        accessToken.setCreatedTime(createdTime);
        return accessToken;
    }
}
