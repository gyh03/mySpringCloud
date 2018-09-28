package com.gyh.security;

import org.springframework.data.redis.connection.RedisPipelineException;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import redis.clients.jedis.JedisCluster;

import java.util.List;

/**
 * JedisClusterConnection 扩展类，重写 openPipeline 方法 ，
 * 依此规避 spring-security-oauth2 不能使用 redis 集群存储的错误
 *
 * @author guoyanhong
 * @date 2018/9/28 16:18
 */
public class JedisClusterConnectionExt extends JedisClusterConnection {
    public JedisClusterConnectionExt(JedisCluster cluster) {
        super(cluster);
    }

    @Override
    public void openPipeline() {
    }

    @Override
    public List<Object> closePipeline() throws RedisPipelineException {
        return null;
    }
}
