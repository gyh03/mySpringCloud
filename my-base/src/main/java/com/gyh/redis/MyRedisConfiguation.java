package com.gyh.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.LinkedHashSet;
import java.util.Set;

@EnableConfigurationProperties(RedisClusterConfig.class)
@Configuration
public class MyRedisConfiguation {
    @Autowired
    private RedisClusterConfig redisClusterConfig;

    @Bean
    public JedisCluster jedisCluster() {
        JedisCluster cluster = createJedisCluster();
        return cluster;
    }

    /**
     * 创建一个 JedisCluster 对象
     *
     * @return
     */
    public JedisCluster createJedisCluster() {
        Set<HostAndPort> hostAndPorts = new LinkedHashSet<HostAndPort>();
        String[] nodeArray = redisClusterConfig.getNodes().split(",");
        for (int i = 0; i < nodeArray.length; i++) {
            String host = nodeArray[i].split(":")[0];
            int port = Integer.parseInt(nodeArray[i].split(":")[1]);
            hostAndPorts.add(new HostAndPort(host, port));
        }
        JedisCluster jedis = new JedisCluster(hostAndPorts,
                redisClusterConfig.getTimeout(), redisClusterConfig.getTimeout(), 1,
                redisClusterConfig.getPassword(), redisClusterConfig.getPool());
        return jedis;
    }
}
