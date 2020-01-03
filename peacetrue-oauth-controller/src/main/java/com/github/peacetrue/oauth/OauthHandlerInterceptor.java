package com.github.peacetrue.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OAuth2.0授权拦截器，拦截请求地址，判断是否已授权（是否存在访问令牌）
 *
 * @author xiayx
 */
public class OauthHandlerInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthorizationHandler authorizationHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("拦截请求地址[{}]，判断是否已授权（OAuth2.0）", request.getServletPath());
        return authorizationHandler.handleAuthorization(request, response);
    }


}
