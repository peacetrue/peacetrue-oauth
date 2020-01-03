package com.github.peacetrue.oauth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 访问令牌
 *
 * @author xiayx
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 0L;

    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String scope;
    private String openId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /** 获取剩余时间(毫秒) */
    public long getRemainedTime() {
        return expiresIn - (System.currentTimeMillis() - createdTime.getTime());
    }
}
