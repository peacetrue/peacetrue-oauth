package com.github.peacetrue.oauth;

import javax.servlet.http.HttpSession;

/**
 * 属性化会话
 *
 * @author xiayx
 */
public interface AttributeHttpSession {

    /**
     * 设置属性
     *
     * @param httpSession 会话
     * @param value       属性值
     */
    void setAttribute(HttpSession httpSession, Object value);

    /**
     * 获取属性值
     *
     * @param httpSession 会话
     * @return 属性值
     */
    Object getAttribute(HttpSession httpSession);

    /**
     * 是否包含指定属性
     *
     * @param httpSession 会话
     * @return true 如果包含，否则 false
     */
    default boolean containAttribute(HttpSession httpSession) {
        return getAttribute(httpSession) != null;
    }

    /**
     * 移除属性
     *
     * @param httpSession 会话
     */
    void removeAttribute(HttpSession httpSession);
}
