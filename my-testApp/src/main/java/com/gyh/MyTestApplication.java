package com.gyh;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class MyTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MyTestApplication.class).web(true).run(args);
        System.out.println("【【【【【【 MyTest 微服务 】】】】】】已启动.");
    }
}
