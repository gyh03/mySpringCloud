package com.gyh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuipeng on 2018/1/23.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private DefaultTokenServices tokenServices;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/demo*",
                "/druid/**"
//                "/checkSmsCode",
//                "/findPassword",
//                "/repetitionMobile",
//                "/registerAlready",
//                "/refreshAccessToken",
//                "/*.png",
//                "/validateCode",
//                "/smscode",
//                "/register",
//                "/login"
        ).permitAll().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        OAuth2AuthenticationProcessingFilter f = new OAuth2AuthenticationProcessingFilter();
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setExceptionTranslator(webResponseExceptionTranslator());
        f.setAuthenticationEntryPoint(oAuth2AuthenticationEntryPoint);
        OAuth2AuthenticationManager o = new OAuth2AuthenticationManager();
//        DefaultTokenServices dt = new DefaultTokenServices();
//        dt.setTokenStore(tokenStore());
        o.setTokenServices(tokenServices);
        f.setAuthenticationManager(o);

        http.addFilterBefore(f,
                AbstractPreAuthenticatedProcessingFilter.class);
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
                    r.put("code", 401);
                    return new ResponseEntity(r, headers, responseEntity.getStatusCode());
//                    return new ResponseEntity(JacksonUtils.toJson(r), headers, responseEntity.getStatusCode());
                } else {
                    return new ResponseEntity(body, headers, responseEntity.getStatusCode());
                }

            }
        };
    }


}
