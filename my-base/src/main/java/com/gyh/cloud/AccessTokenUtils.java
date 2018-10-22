package com.gyh.cloud;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author guoyanhong
 * @date 2018/10/17 21:23
 */
public class AccessTokenUtils {
    public static final String Authorization = "Authorization";
    /**
     * 从当前 request 中获取当前用户 access_token
     * @return
     */
    public static String getUserToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String accessToken = request.getParameter("access_token");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter("accessToken");
        }
        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getHeader(Authorization);
        }
        return accessToken;
    }
}
