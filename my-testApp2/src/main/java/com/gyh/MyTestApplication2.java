package com.gyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
//@SpringCloudApplication
@SpringBootApplication
public class MyTestApplication2 {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MyTestApplication2.class).run(args);
//        SpringApplication.run(MyTestApplication2.class,args);
        System.out.println("【【【【【【 MyTest2 微服务 】】】】】】已启动.");
    }
}
