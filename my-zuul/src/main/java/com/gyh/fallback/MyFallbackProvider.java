package com.gyh.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Zuul熔断功能
 * zuul进行路由分发时，如果后端服务没有启动，或者调用超时，
 * 这时候我们希望Zuul提供一种降级功能，而不是将异常暴露出来。
 * <p>
 * 需要注意的是，这个熔断器不支持以url配置的路由，必须要用serviceId的方式路由的方式才能使用熔断器
 */
public class MyFallbackProvider implements FallbackProvider {
    private static Logger log = LoggerFactory.getLogger(MyFallbackProvider.class);
    private String route = null;
    //服务不可用
    private int rawStatusCode = 503;
    private HttpStatus statusCode = HttpStatus.SERVICE_UNAVAILABLE;
    private String statusText = "Service Unavailable";

    /**
     * api服务id，如果需要所有调用都支持回退，则return "*"或return null
     *
     * @return
     */
    @Override
    public String getRoute() {
        if (route == null) {
            route = "route";
        }
        return route;
    }

    /**
     * 如果请求用户服务失败，返回什么信息给消费者客户端
     * 定义一个ClientHttpResponse作为当异常出现时的返回内容
     *
     * @return
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            /**
             * 网关向api服务请求是失败了，但是消费者客户端向网关发起的请求是OK的，
             * 不应该把api的404,500等问题抛给客户端
             * 网关和api服务集群对于客户端来说是黑盒子
             */
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                log.info("rawStatusCode: " + rawStatusCode);
                return rawStatusCode;
            }

            @Override
            public String getStatusText() throws IOException {
                if (statusText == null) {
                    statusText = "Service Unavailable";
                }
                log.info("statusText：" + statusText);
                return statusText;
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("return message".getBytes("UTF-8"));
            }

            @Override
            public HttpHeaders getHeaders() {
                //和body中的内容编码一致，否则容易乱码
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }


    public void setRoute(String route) {
        this.route = route;
    }

    public int getRawStatusCode() {
        return rawStatusCode;
    }

    public void setRawStatusCode(int rawStatusCode) {
        this.rawStatusCode = rawStatusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
