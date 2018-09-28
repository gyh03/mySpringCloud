package com.gyh.mycatDemo;


import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MyRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MyRunnable.class);


    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(120000)
            .setSocketTimeout(60000)
            .setConnectionRequestTimeout(60000)
            .setCookieSpec(CookieSpecs.STANDARD_STRICT)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
    CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    private static HttpClientContext context = HttpClientContext.create();
//    CloseableHttpClient httpClient=HttpClientPool.getHttpClient();
//   HttpPost send = new HttpPost("http://59.110.146.137:9999/platform-app/platform/register"); http://59.110.146.137:9999/register
       HttpPost send = new HttpPost("http://59.110.146.137:9999/register");

//    HttpPost send = new HttpPost("http://localhost:9095/hello");
//    HttpPost send = new HttpPost("http://localhost/myreg/platform-app/platform/register");

//    HttpPost send = new HttpPost("http://localhost:9094/register");
//HttpGet send = new HttpGet("http://localhost:9999/platform-app/hello/hello");
//    HttpGet send = new HttpGet("http://10.17.9.19:9999/platform-app/spring-cloud-producer/hello?name=test");

//        HttpGet send = new HttpGet("http://localhost:9999/order/demo");
//    HttpPost httpPost = new HttpPost("http://192.168.3.157:9999/platform-app/platform/login");
//    HttpPost httpPost = new HttpPost("http://localhost:9999/platform-app/platform/tendUser");
    @Override
    public void run() {
        for (int x = 0; x < 1; x++) {
            try {
//                //带授权token，json参数
                JSONObject jsonParam = new JSONObject();
////                jsonParam.put("mobile", "13077777777");
////                jsonParam.put("password", "1111");
                jsonParam.put("mobile",String.valueOf(13000000000l+this.name));
                jsonParam.put("password","18e0e5f3389efa0b31738c2fd44630d8");
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//解决中文乱码问题
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                send.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
//                send.setHeader("Authorization","Bearer 5c35a13e-19eb-4784-90f1-4c7900c336b0");
                send.setEntity(entity);

                CloseableHttpResponse response = httpClient.execute(send, context);
                HttpEntity httpEntity = response.getEntity();
                String body = "";
                if (httpEntity != null) {
                    body = EntityUtils.toString(httpEntity, Consts.UTF_8);
                    logger.info("mobile is {},body is{},getStatusCode is{}",String.valueOf(13000000000l+this.name),body,response.getStatusLine().getStatusCode()
                    );
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }finally {
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    logger.error(e.getMessage());
//                }
            }
//            if (x % 10000 == 0) {
//                logger.info("{}, {}", Thread.currentThread().getName(), x);
//            }
        }
    }
    private long name=0;
    public MyRunnable(int name){
        this.name=name;

    }

}

// class MutiThread {
//    private static final Logger logger = LoggerFactory.getLogger(MutiThread.class);
//
////    public static void main(String[] args) throws Exception {
////        //创建一个线程池对象，控制要创建几个线程对象。
////        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
////        List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
////        for (ch.qos.logback.classic.Logger logger:loggerList){
////            logger.setLevel(Level.toLevel("info"));
////        }
////        ExecutorService pool = Executors.newFixedThreadPool(5000);
////       for (int i = 1000000; i < 1000000000; i++) {
////            try {
////                pool.submit(new MyRunnable(i));
//////                if(i%100==0) {
//////                    Thread.sleep(1000);
//////                }
////            } catch (Exception e) {
////                continue;
////            }
////        }
////        //结束线程池
////        pool.shutdown();
////    }
//}


//        for (int x = 0; x < 1000000000; x++) {
//
//            try {
//                Demo.insertMycat(String.valueOf(x), UUID.randomUUID().toString());
//            }catch (Exception e){
//                continue;
//            }
//            if(x%100==0) {
//                logger.info("{}, {}", Thread.currentThread().getName(), "x");
//             }
//        }
//                Demo.insertMycat(String.valueOf(x), UUID.randomUUID().toString());

// 可以执行Runnable对象或者Callable对象代表的线程

//        List<NameValuePair> values = new ArrayList<NameValuePair>();
//        values.add(new BasicNameValuePair("grant_type", "password"));
//        values.add(new BasicNameValuePair("username", p.get("mobile").toString()));
//        values.add(new BasicNameValuePair("password", p.get("password").toString()));
//        values.add(new BasicNameValuePair("scope", loginScope));

//        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Consts.UTF_8);
//        httpPost.setEntity(entity);
//        httpPost.setHeader("authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes("utf-8")));
