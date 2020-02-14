package com.gyh.controller;

import com.gyh.feign.FirstFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author guoyanhong
 * @date 2018/9/28 17:12
 */
@RestController
public class ClientController {

    @Autowired
    private FirstFeignService firstFeignService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("getSomeMsg")
    public Object getSomeMsg() {
        return "feign msg >> " + firstFeignService.getSomeMsg();
    }

    @GetMapping("getSomeMsg2")
    public Object getSomeMsg2() {
        return "restTemplate msg >> " + restTemplate.getForObject("http://myTestApp/getSomeMsg", Object.class);
    }
}
