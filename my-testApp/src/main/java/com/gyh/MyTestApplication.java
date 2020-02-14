package com.gyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class MyTestApplication {

    public static void main(String[] args) {
//        new SpringApplication(MyTestApplication.class).run(args);
        new SpringApplicationBuilder(MyTestApplication.class).run(args);
        System.out.println("【【【【【【 MyTest 微服务 】】】】】】已启动.");
    }
}
