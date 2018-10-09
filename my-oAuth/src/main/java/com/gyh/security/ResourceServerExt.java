package com.gyh.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源服务配置
 * 一个资源服务（可以和授权服务在同一个应用中，当然也可以分离开成为两个不同的应用程序）提供一些受token令牌保护的资源，
 * @EnableResourceServer 注解自动增加了一个类型为 OAuth2AuthenticationProcessingFilter 的过滤器链，来实现验证保护
 *
 * @author guoyanhong
 * @date 2018/9/29 9:52
 */
@Configuration
@EnableResourceServer
public class ResourceServerExt extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 过滤 /druid/** 请求，不需验证
        http.authorizeRequests().antMatchers(
                "/druid/**"
        ).permitAll().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

    }
}
