package com.github.peacetrue.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpSession;

/**
 * 接口控制器
 *
 * @author xiayx
 */
@RequestMapping
public class ApiOauthController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OauthService oauthService;
    @Autowired
    private AttributeHttpSession oauthAccessTokenHttpSession;
    @Autowired
    private AttributeHttpSession oauthTargetUrlHttpSession;

    /**
     * 生成获取认证码的链接
     *
     * @param scope 作用域
     * @return 获取认证码的链接
     */
    @ResponseBody
    @GetMapping(value = "${peacetrue.oauth.url4GenerateAuthorizationUrl:/oauth/authorization/url}")
    public String generateAuthorizationUrl(HttpSession httpSession,
                                           @RequestParam String scope,
                                           @RequestParam String targetUrl) {
        logger.info("根据作用域[{}]生成【授权链接】", scope);
        oauthTargetUrlHttpSession.setAttribute(httpSession, targetUrl);
        return oauthService.generateAuthorizationUrl(scope);
    }

    /**
     * 获取访问令牌
     *
     * @param httpSession 会话
     * @return 访问令牌
     */
    @Nullable
    @ResponseBody
    @GetMapping("/oauth/access-token")
    public AccessToken getAccessToken(HttpSession httpSession) {
        logger.info("获取会话[{}]对应的访问令牌", httpSession.getId());
        return (AccessToken) oauthAccessTokenHttpSession.getAttribute(httpSession);
    }

    /**
     * 刷新访问令牌
     *
     * @param httpSession 会话
     * @return 访问令牌
     */
    @ResponseBody
    @PostMapping("/oauth/access-token/refresh")
    public AccessToken refreshAccessToken(HttpSession httpSession) {
        logger.info("刷新会话[{}]对应的访问令牌", httpSession.getId());
        AccessToken accessToken = (AccessToken) oauthAccessTokenHttpSession.getAttribute(httpSession);
        accessToken = oauthService.refreshAccessToken(accessToken.getRefreshToken());
        oauthAccessTokenHttpSession.setAttribute(httpSession, accessToken);
        return accessToken;
    }

}

