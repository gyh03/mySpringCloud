package com.gyh.oauth;

import com.gyh.redis.JedisClusterMaker;
import com.gyh.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * 资源服务配置
 * 一个资源服务（可以和授权服务在同一个应用中，当然也可以分离开成为两个不同的应用程序）提供一些受token令牌保护的资源，
 * EnableResourceServer 注解自动增加了一个类型为 OAuth2AuthenticationProcessingFilter 的过滤器链，来实现验证保护
 *
 * @author guoyanhong
 * @date 2018/10/8 10:19
 */
@Configuration
@EnableResourceServer
public class ResourceServerExt extends ResourceServerConfigurerAdapter {
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private JedisClusterMaker jedisClusterMaker;

    @Bean
    public RedisTokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
    }

    /**
     * 其他的自定义权限保护规则通过 HttpSecurity 来进行配置。
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //解决Spring Boot不允许加载iframe问题
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers(
                "/loginBeforeScan",
                "/druid/**",
                "/scanUUID",
                "/message/send").permitAll().and().csrf().disable().exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and().authorizeRequests().anyRequest()
                .authenticated().and().httpBasic();

    }

    /**
     * 从 connectionFactory 获取 JedisCluster，如果没有则创建一个
     * @return
     */
    @Bean
    public JedisCluster jedisCluster() {
        JedisCluster cluster = null;
        RedisConnection redisConnection = connectionFactory.getConnection();
        if (redisConnection instanceof JedisClusterConnection) {
            cluster = (JedisCluster) redisConnection.getNativeConnection();
        }else{
            cluster = jedisClusterMaker.createJedisCluster();
        }
        return cluster;
    }

    /**
     * ResourceServerTokenServices 类的实例,用来实现令牌服务
     * 资源服务值针对 token 进行读取验证操作，无需设置过期时间
     *
     * @return
     */
    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // defaultTokenServices.setAccessTokenValiditySeconds(-1);
        // defaultTokenServices.setRefreshTokenValiditySeconds(-1);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

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
                    r.put("status", 2900);
                    String resp = JacksonUtils.toJson(r);
                    return new ResponseEntity(resp, headers, responseEntity.getStatusCode());
                } else {
                    return new ResponseEntity(body, headers, responseEntity.getStatusCode());
                }

            }
        };
    }

}