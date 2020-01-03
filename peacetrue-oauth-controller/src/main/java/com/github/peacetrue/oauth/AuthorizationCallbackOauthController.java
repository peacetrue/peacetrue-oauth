package com.github.peacetrue.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 授权通过后回调控制器
 *
 * @author xiayx
 */
@RequestMapping
public class AuthorizationCallbackOauthController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OauthService oauthService;
    @Autowired
    private AttributeHttpSession oauthAccessTokenHttpSession;
    @Autowired
    private AttributeHttpSession oauthTargetUrlHttpSession;
    @Value("${peacetrue.oauth.autoRefresh:true}")
    private Boolean enableAutoRefresh;
    @Autowired(required = false)
    private ScheduledExecutorService oauthScheduledExecutorService;
    @Value("${peacetrue.oauth.prerefreshTime:60000}")
    private Integer prerefreshTime;

    /**
     * 处理授权通过后的回调。接收第三方应用返回的认证码
     *
     * @param state 状态随机串 //TODO 校验状态随机串
     * @param code  认证码
     */
    @GetMapping(value = "${peacetrue.oauth.authorizationCallbackUrl:}", params = {"state", "code"})
    public String handleAuthorizationCallback(HttpSession httpSession, @RequestParam String code, String state) {
        logger.info("处理会话[{}]授权通过后的回调", httpSession.getId());
        AccessToken accessToken = oauthService.getAccessToken(code);
        logger.debug("取得认证码[{}]对应的访问令牌[{}]", code, accessToken);
        oauthAccessTokenHttpSession.setAttribute(httpSession, accessToken);
        Object targetUrl = oauthTargetUrlHttpSession.getAttribute(httpSession);
        logger.debug("会话[{}]取得访问令牌后，重定向至目标页[{}]", httpSession.getId(), targetUrl);
        if (enableAutoRefresh && oauthScheduledExecutorService != null) {
            this.enableAutoRefreshAccessToken(httpSession);
        }
        return "redirect:" + targetUrl;
    }


    /**
     * 启用自动刷新访问令牌
     *
     * @param httpSession 会话
     */
//    @ResponseBody
//    @PostMapping("/oauth/access-token/enable/auto/refresh")
    public void enableAutoRefreshAccessToken(HttpSession httpSession) {
        logger.info("启用自动刷新会话[{}]对应的访问令牌", httpSession.getId());

        String attributeName = "oauth.autoRefresh";
        if (httpSession.getAttribute(attributeName) != null) {
            logger.debug("会话[{}]已经启用了自动刷新访问令牌", httpSession.getId());
            return;
        }
        httpSession.setAttribute(attributeName, true);

        AccessToken accessToken = (AccessToken) oauthAccessTokenHttpSession.getAttribute(httpSession);
        long period = accessToken.getExpiresIn() - prerefreshTime;
        long passedTime = System.currentTimeMillis() - accessToken.getCreatedTime().getTime();
        oauthScheduledExecutorService.scheduleAtFixedRate(() -> {
            logger.debug("自动刷新会话[{}]对应的访问令牌", httpSession.getId());
            AccessToken refreshAccessToken = oauthService.refreshAccessToken(accessToken.getRefreshToken());
            oauthAccessTokenHttpSession.setAttribute(httpSession, refreshAccessToken);
        }, period - passedTime, period, TimeUnit.MILLISECONDS);
    }

}
