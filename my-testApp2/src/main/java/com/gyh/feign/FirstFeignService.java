package com.gyh.feign;

//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author guoyanhong
 * @date 2018/10/17 16:01
 */
@FeignClient("myTestApp")
public interface FirstFeignService {

    @GetMapping("getSomeMsg")
    Object getSomeMsg() ;
}
