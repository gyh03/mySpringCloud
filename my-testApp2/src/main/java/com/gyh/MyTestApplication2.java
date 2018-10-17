package com.gyh;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class MyTestApplication2 {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MyTestApplication2.class).web(true).run(args);
        System.out.println("【【【【【【 MyTest2 微服务 】】】】】】已启动.");
    }
}
