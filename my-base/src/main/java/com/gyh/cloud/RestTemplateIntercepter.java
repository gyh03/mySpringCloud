package com.gyh.cloud;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * 自定义 RestTemplate 拦截器，使用 RestTemplate 对象时经过此拦截器，传递access_token
 *
 * @author guoyanhong
 * @date 2018/10/17 21:19
 */
public class RestTemplateIntercepter implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        String access_token = AccessTokenUtils.getUserToken();
        // 设置 header 将用户登录信息及其他信息传递到下层
        requestWrapper.getHeaders().add(AccessTokenUtils.Authorization, "Bearer " + access_token);
        return execution.execute(requestWrapper, body);
    }
}
