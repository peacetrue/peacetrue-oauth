package com.github.peacetrue.oauth;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * 属性化会话实现
 *
 * @author xiayx
 */
@NoArgsConstructor
public class AttributeHttpSessionImpl implements AttributeHttpSession {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private String attributeName;

    public AttributeHttpSessionImpl(String attributeName) {
        this.setAttributeName(attributeName);
    }

    @Override
    public void setAttribute(HttpSession httpSession, Object value) {
        logger.debug("设置会话[{}]属性[{}]=[{}]", httpSession.getId(), attributeName, value);
        httpSession.setAttribute(attributeName, value);
    }

    @Override
    public Object getAttribute(HttpSession httpSession) {
        return httpSession.getAttribute(attributeName);
    }

    @Override
    public void removeAttribute(HttpSession httpSession) {
        logger.debug("设置会话[{}]属性[{}]", httpSession.getId(), attributeName);
        httpSession.removeAttribute(attributeName);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = Objects.requireNonNull(attributeName);
    }
}
