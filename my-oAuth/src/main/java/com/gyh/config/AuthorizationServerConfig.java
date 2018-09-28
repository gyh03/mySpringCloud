package com.gyh.config;

import com.gyh.security.JedisConnectionFactoryExt;
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

/**
 * Created by cuipeng on 2018/1/23.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

        //@Autowired
        //private AuthorizationEndpoint authorizationEndpoint;
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
	private UserDetailsService userDetailsService;
    @Value("${gyh.oauth.clientid}")
    private String clientId;
    @Value("${gyh.oauth.secret}")
    private String secret;
    @Primary
    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setAccessTokenValiditySeconds(60*60*10); // 有效24小时，设定为-1表示token不过期
        defaultTokenServices.setRefreshTokenValiditySeconds(60*60*24*30);// 有效期30天 -1表示token不过期
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }


    @Bean
    public RedisTokenStore tokenStore() {
        JedisConnectionFactoryExt connectionFactoryExt = new JedisConnectionFactoryExt(connectionFactory);
        return new RedisTokenStore(connectionFactoryExt);
//        return new RedisTokenStore(connectionFactory);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)//若无，refresh_token会有UserDetailsService is required错误
                .tokenStore(tokenStore());
        endpoints.tokenServices(tokenServices());// token 自定义
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .scopes("gyh")
                .secret(secret)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token");
//            .and()
//                .withClient("webapp")
//                .scopes("xx")
//                .authorizedGrantTypes("implicit");
    }
}
