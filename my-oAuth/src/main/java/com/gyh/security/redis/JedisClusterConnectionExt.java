package com.gyh.security.redis;

import org.springframework.data.redis.connection.RedisPipelineException;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import redis.clients.jedis.JedisCluster;

import java.util.List;

/**
 * JedisClusterConnection 扩展类，重写 openPipeline 方法 ，
 * 依此规避 spring-security-oauth2 不支持 redis 集群存储的错误。
 * spring-security-oauth2 的 RedisTokenStore 中会开启链接的管道：conn.openPipeline();
 * 而当链接 conn 是Redis集群时即 JedisClusterConnection ，openPipeline() 方法会直接抛出异常(因为JedisCluter不能直接打开管道，Jedis实例才可以)；
 * 而使用集群链接操作时，不需要用到 Pipeline （单机JedisConnection会用到），
 * 所以此处重写 JedisClusterConnection 的 openPipeline() 和 closePipeline() 方法，使之不抛出异常，可以正常用Redis集群存储oAuth数据；
 * 配合 JedisConnectionFactoryExt 使用。
 * <p>
 * 网上有针对这个spring的bug其他修改方法，例如实现 TokenStore 接口，重写其方法，但那种方法改动过大。
 * RedisTokenStore 本身已经很好的实现了Redis存储token，只是在 conn.openPipeline(); 之前没有判断是否是 JedisClusterConnection 所以造成不支持 Redis 集群的现象，
 * 如果继承 RedisTokenStore 类，重写 openPipeline() 的地方，也是一种方法，但改动量较大，此处添加两个扩展类，以最小的代价实现功能。
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
