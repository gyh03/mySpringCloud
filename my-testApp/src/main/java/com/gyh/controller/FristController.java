package com.gyh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.Map;

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
        Map<String,String> res = new HashMap<>(1);
        res.put("name",jedisCluster.get("myname"));
        return res;
    }
}
