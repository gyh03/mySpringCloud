package com.gyh.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * spring oAuth2 配置信息
 *
 * @author guoyanhong
 * @date 2018/9/29 21:19
 */
@ConfigurationProperties(prefix = "gyh.oauth")
@Component
@Data
public class AuthorizationServerConfig {
    /**
     *
     */
    private String clientId;
    /**
     *
     */
    private String secret;
    /**
     *
     */
    private String scopes;
    private List<String> authorizedGrantTypes;
    /**
     * accessToken 过期时间
     */
    private int accessTokenSeconds;
    /**
     * refreshToken 过期时间
     */
    private int refreshTokenSeconds;
}
