package com.gyh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

/**
 * @author guoyanhong
 * @date 2018/9/28 17:12
 */
@RestController
public class FristController {

    @Autowired
    private JedisCluster jedisCluster;

    @GetMapping("getSomeMsg")
    public Object getSomeMsg() {
        jedisCluster.set("myname", "gyh");
        return "i`m your father ,my name is " + jedisCluster.get("myname");
    }
}
