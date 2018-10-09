package com.gyh;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author guoyanhong
 * @date 2018/9/30 22:00
 */
public class RedisClusterTest {


    public static void main(String[] args) {

        Set<HostAndPort> jedisClusterNode = new HashSet<>();
        jedisClusterNode.add(new HostAndPort("112.74.181.24", 7001));
        jedisClusterNode.add(new HostAndPort("112.74.181.24", 7002));
        jedisClusterNode.add(new HostAndPort("112.74.181.24", 7003));
        jedisClusterNode.add(new HostAndPort("112.74.181.24", 7004));
        jedisClusterNode.add(new HostAndPort("112.74.181.24", 7005));
        jedisClusterNode.add(new HostAndPort("112.74.181.24", 7006));
        //GenericObjectPoolConfig goConfig = new GenericObjectPoolConfig();
        //JedisCluster jc = new JedisCluster(jedisClusterNode,2000,100, goConfig);
        JedisPoolConfig cfg = new JedisPoolConfig();
        cfg.setMaxTotal(100);
        cfg.setMaxIdle(20);
        cfg.setMaxWaitMillis(-1);
        cfg.setTestOnBorrow(true);
        JedisCluster jc = new JedisCluster(jedisClusterNode,6000,6000,1000,"123456",cfg);

        System.out.println(jc.set("age","20"));
        System.out.println(jc.set("name","yfs"));
        System.out.println(jc.set("sex","男"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("age"));
        System.out.println(jc.get("sex"));
        try {
            jc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}