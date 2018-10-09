package com.gyh.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 自定义 JedisCluster 配置
 * 这里使用了 Spring OAuth 存储token 用的 redis 配置
 * 建议配置成标准的spring cluster 配置文件
 * @author guoyanhong
 * @date 2018/10/9 18:27
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class JedisClusterMaker {
    private String password;
    private JedisPoolConfig pool;
    private int timeout;
    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    /**
     * 创建一个 JedisCluster 对象
     *
     * @return
     */
    public JedisCluster createJedisCluster() {
        Set<HostAndPort> hostAndPorts = new LinkedHashSet<HostAndPort>();
        String[] nodeArray = nodes.split(",");
        for (int i = 0; i < nodeArray.length; i++) {
            String host = nodeArray[i].split(":")[0];
            int port = Integer.parseInt(nodeArray[i].split(":")[1]);
            hostAndPorts.add(new HostAndPort(host, port));
        }
       JedisCluster jedis = new JedisCluster(hostAndPorts, timeout, timeout, 1, password, pool);
        return jedis;
    }
}
