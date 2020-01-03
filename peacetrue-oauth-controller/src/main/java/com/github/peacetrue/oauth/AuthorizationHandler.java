package com.github.peacetrue.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 授权认证处理器
 *
 * @author xiayx
 */
public class AuthorizationHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttributeHttpSession oauthAccessTokenHttpSession;
    @Autowired
    private AttributeHttpSession oauthTargetUrlHttpSession;
    @Autowired
    private OauthService oauthService;

    /**
     * 处理授权
     *
     * @param request  请求
     * @param response 响应
     * @return true 如果已经授权了；否则 false
     * @throws ServletException 参数异常
     * @throws IOException      重定向异常
     */
    public boolean handleAuthorization(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetUrl = request.getRequestURL().toString() + "?" + request.getQueryString();
        return handleAuthorization(request, response, targetUrl);
    }

    /**
     * 处理授权
     *
     * @param request  请求
     * @param response 响应
     * @param targetUrl
     * @return true 如果已经授权了；否则 false
     * @throws ServletException 参数异常
     * @throws IOException      重定向异常
     */
    public boolean handleAuthorization(HttpServletRequest request, HttpServletResponse response, String targetUrl) throws ServletException, IOException {
        if (oauthAccessTokenHttpSession.containAttribute(request.getSession())) {
            logger.debug("请求地址[{}]已经通过授权", request.getServletPath());
            return true;
        }
        this.redirectToAuthorizationPage(request, response, targetUrl);
        return false;
    }

    /**
     * 跳转至授权页
     *
     * @param request  请求
     * @param response 响应
     * @throws ServletException 参数异常
     * @throws IOException      重定向异常
     */
    public void redirectToAuthorizationPage(HttpServletRequest request, HttpServletResponse response, String targetUrl) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        String scope = request.getParameter("scope");
        if (scope == null) throw new MissingServletRequestParameterException("scope", String.class.getName());
        String authorizationUrl = oauthService.generateAuthorizationUrl(scope);
        logger.debug("会话[{}]访问令牌不存在，跳转至授权页[{}]", httpSession.getId(), authorizationUrl);
        oauthTargetUrlHttpSession.setAttribute(httpSession, targetUrl);
        response.sendRedirect(authorizationUrl);
    }
}
