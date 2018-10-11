package com.gyh.security;

import com.gyh.security.redis.JedisConnectionFactoryExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授权服务配置
 *
 * @author guoyanhong
 * @date 2018/9/29 9:57
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerExt implements AuthorizationServerConfigurer {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private DomainUserDetailsService userDetailsService;

    @Autowired
    private AuthorizationServerConfig authorizationServerConfig;


    /**
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                // 注入自定义 DomainUserDetailsService，实现从数据库查询用户以校验密码
                .userDetailsService(userDetailsService)
                // 自定义tokenServices，DefaultTokenServices 接口定义了一些操作使得你可以对令牌进行一些必要的管理
                .tokenServices(tokenServices())
                // 自定义 tokenStore ，实现 redis 集群持久化token
                .tokenStore(tokenStore());
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 配置客户端详情服务（ClientDetailsService）
     * 客户端详情信息在这里进行初始化， 你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
     * clientId：（必须的）用来标识客户的Id。
     * secret：（需要值得信任的客户端）客户端安全码，如果有的话。
     * scope：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
     * authorizedGrantTypes：此客户端可以使用的授权类型，默认为空。
     * authorities：此客户端可以使用的权限（基于Spring Security authorities）。
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        List<String> authTypes = authorizationServerConfig.getAuthorizedGrantTypes();
        String[] authTypeArr = authTypes.toArray(new String[authTypes.size()]);
        clients.inMemory()
                .withClient(authorizationServerConfig.getClientId())
                .secret(authorizationServerConfig.getSecret())
                .scopes(authorizationServerConfig.getScopes())
                .authorizedGrantTypes(authTypeArr);
    }

    /**
     * 自定义 tokenServices
     * ResourceServerTokenServices 类的实例,用来实现令牌服务
     *
     * @return
     */
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

    /**
     * 自定义 tokenStore
     *
     * @return
     */
    @Bean
    public RedisTokenStore tokenStore() {
        JedisConnectionFactoryExt connectionFactoryExt = new JedisConnectionFactoryExt(connectionFactory);
        return new RedisTokenStore(connectionFactoryExt);
    }

    /**
     * 密码校验器，用于校验密码
     * 可以自定义一个类实现接口 PasswordEncoder，重写其方法，实现自定义密码加密校验规则
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 自定义异常处理器
     *
     * @return
     */
    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity translate(Exception e) throws Exception {
                ResponseEntity responseEntity = super.translate(e);
                OAuth2Exception body = (OAuth2Exception) responseEntity.getBody();
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                // do something with header or response
                if (401 == responseEntity.getStatusCode().value()) {
                    Map r = new HashMap();
                    r.put("message", "Invalid access token");
                    r.put("code", 401);
                    return new ResponseEntity(r, headers, responseEntity.getStatusCode());
                } else {
                    return new ResponseEntity(body, headers, responseEntity.getStatusCode());
                }

            }
        };
    }

}
