package com.gyh.api;

import com.gyh.feign.FirstFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

/**
 * @author guoyanhong
 * @date 2018/9/28 17:12
 */
@RestController
public class ClientController {

    @Autowired
    private FirstFeignService firstFeignService;

    @GetMapping("getSomeMsg")
    public Object getSomeMsg() {
        return "feign msg >> " + firstFeignService.getSomeMsg();
    }
}
