package com.gyh.cloud;

import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign请求方式将信息向下传递 header
 *
 * @author guoyanhong
 * @date 2018/10/17 16:57
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(FeignRequestInterceptor.class);

    private static final String Feign = "Feign";

    @Override
    public void apply(feign.RequestTemplate requestTemplate) {
        try {
            String access_token = AccessTokenUtils.getUserToken();
            // 设置 header 将用户登录信息及其他信息传递到下层
            requestTemplate.header(AccessTokenUtils.Authorization, "Bearer " + access_token);
            // 标识该请求是feign接口的请求
            requestTemplate.header(Feign, Feign);
            logger.info("Feign请求方式传递 access_token = " + access_token);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("feignError is {}", e.getMessage());
        }
    }

}
