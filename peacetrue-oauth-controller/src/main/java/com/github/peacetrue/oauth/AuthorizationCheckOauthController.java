package com.github.peacetrue.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权检查控制器
 *
 * @author xiayx
 */
@RequestMapping
public class AuthorizationCheckOauthController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AuthorizationHandler authorizationHandler;

    /**
     * 检查是否授权
     *
     * @param request   请求
     * @param response  响应
     * @param targetUrl 授权完成后的目标地址
     * @throws ServletException 参数异常
     * @throws IOException      重定向异常
     */
    @GetMapping(value = "${peacetrue.oauth.authorizationCheckUrl:/oauth/authorization/check}")
    public void checkAuthorization(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestParam String targetUrl) throws ServletException, IOException {
        logger.info("检查会话[{}]对应的访问令牌是否已授权", request.getRequestedSessionId());
        if (authorizationHandler.handleAuthorization(request, response, targetUrl)) {
            response.sendRedirect(targetUrl);
        }
    }

}
