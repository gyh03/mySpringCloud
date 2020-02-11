package com.gyh;


import com.gyh.security.SecurityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

@SpringCloudApplication
//@EnableFeignClients
public class OAuthApplication {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "auditorAware")
    public AuditorAware<String> auditorAware() {
        return () -> java.util.Optional.ofNullable(SecurityUtils.getCurrentUserUsername());
    }


    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication.class, args);
        System.out.println("【【【【【【 OAuthApplication 微服务 】】】】】】已启动.");
    }

}
