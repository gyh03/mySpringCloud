package com.gyh;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaApplication.class).web(true).run(args);
        System.out.println("【【【【【【 EurekaApplication 微服务 】】】】】】已启动.");
    }
}
