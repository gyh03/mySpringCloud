package com.gyh.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author guoyanhong
 * @date 2018/9/29 10:03
 */
@Component
@ConfigurationProperties(prefix = "gyh.oauth")
@RefreshScope
@Data
public class AuthorizationServerConfig {
    private String clientId;
    private String secret;
    private String scopes;
    private int accessTokenSeconds;
    private int refreshTokenSeconds;
    private List<String> authorizedGrantTypes;
}
