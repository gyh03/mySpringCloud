package com.gyh.mycatDemo;

import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Arrays;

public class HttpClientPool {
    private static PoolingHttpClientConnectionManager cm;
    private static RequestConfig globalConfig = RequestConfig.custom()
            .setConnectTimeout(120000)
            .setSocketTimeout(60000)
            .setConnectionRequestTimeout(60000)
            .setCookieSpec(CookieSpecs.STANDARD_STRICT)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
    static {
        cm = null;
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(600);
        cm.setDefaultMaxPerRoute(10);
    }
    public static CloseableHttpClient getHttpClient(){
//        RequestConfig globalConfig = RequestConfig.custom()
//                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        CloseableHttpClient client = HttpClientBuilder.create().setConnectionManager(cm).setDefaultRequestConfig(globalConfig).build();

        return client;
    }
}
