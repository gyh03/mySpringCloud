package com.gyh.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.List;

/**
 * 自定义 security 对象，扩展代码实现自己的功能
 *
 * @author guoyanhong
 * @date 2018/9/29 9:57
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurerExt extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private DomainUserDetailsService userDetailsService;

    @Autowired
    private AuthorizationServerConfig authorizationServerConfig;


    @Primary
    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // -1表示token不过期
        defaultTokenServices.setAccessTokenValiditySeconds(authorizationServerConfig.getAccessTokenSeconds());
        // -1表示token不过期
        defaultTokenServices.setRefreshTokenValiditySeconds(authorizationServerConfig.getRefreshTokenSeconds());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }


    @Bean
    public RedisTokenStore tokenStore() {
        JedisConnectionFactoryExt connectionFactoryExt = new JedisConnectionFactoryExt(connectionFactory);
        return new RedisTokenStore(connectionFactoryExt);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                // 注入自定义 DomainUserDetailsService，实现从数据库查询用户以校验密码
                .userDetailsService(userDetailsService)
                // 自定义 tokenStore ，实现 redis 集群存储token
                .tokenStore(tokenStore())
                // 自定义tokenServices，配置 token 过期时间
                .tokenServices(tokenServices());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        List<String> authTypes = authorizationServerConfig.getAuthorizedGrantTypes();
        String[] authTypeArr = authTypes.toArray(new String[authTypes.size()]);
        clients.inMemory()
                .withClient(authorizationServerConfig.getClientId())
                .scopes(authorizationServerConfig.getScopes())
                .secret(authorizationServerConfig.getSecret())
                .authorizedGrantTypes(authTypeArr);
    }
}
