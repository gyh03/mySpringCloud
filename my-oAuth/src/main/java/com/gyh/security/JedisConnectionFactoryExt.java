package com.gyh.security;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisCluster;

/**
 * 扩展 JedisConnectionFactory 类
 * 重写 getConnection 方法，当 RedisConnection 为集群类型时，使用自定义 JedisClusterConnectionExt 类
 *
 * @author guoyanhong
 * @date 2018/9/28 16:22
 */
public class JedisConnectionFactoryExt extends JedisConnectionFactory {
    private JedisConnectionFactory jedisConnectionFactory;

    public JedisConnectionFactoryExt(RedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = (JedisConnectionFactory) jedisConnectionFactory;
    }

    /**
     * 重写 getConnection 方法，当 RedisConnection 为集群类型时，使用自定义 JedisClusterConnectionExt 类，作用参见此类注释
     *
     * @return
     */
    @Override
    public RedisConnection getConnection() {
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        if (redisConnection instanceof JedisClusterConnection) {
            JedisCluster cluster = (JedisCluster) redisConnection.getNativeConnection();
            redisConnection = new JedisClusterConnectionExt(cluster);
        }
        return redisConnection;
    }
}
