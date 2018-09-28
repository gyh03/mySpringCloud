package com.gyh.mycatDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashSet;
import java.util.Set;


public class MyJedisCluster {

    private static final Logger logger = LoggerFactory.getLogger(MyJedisCluster.class);

    private static String host;
    private static int port;
    private static String password;

    private static JedisPoolConfig config;
    private static JedisPool jedisPool;

    private static int connectionTimeout;
    private static int soTimeout;


    private static JedisCluster jedis;

    private static String nodes;

    // private static JedisPool getJedisPool() {
    // if (jedisPool == null) {
    // jedisPool = new JedisPool(getRedisConfig(), host, port, 15000, password, 1);
    // }
    // return jedisPool;
    // }

    private static JedisCluster getJedis() {
        if (config == null) {
            config = new JedisPoolConfig();
            config.setMaxTotal(32);// 最大连接数
            config.setMaxIdle(6);// 闲置最大连接数
            config.setMinIdle(0);// 闲置最小连接数
            config.setMaxWaitMillis(15000);// 到达最大连接数后，调用者阻塞时间
            config.setMinEvictableIdleTimeMillis(300000);// 连接空闲的最小时间，可能被移除
            config.setSoftMinEvictableIdleTimeMillis(-1);// 连接空闲的最小时间，多余最小闲置连接的将被移除
            config.setNumTestsPerEvictionRun(3);// 设置每次检查闲置的个数
            config.setTestOnBorrow(false);// 申请连接时，是否检查连接有效
            config.setTestOnReturn(false);// 返回连接时，是否检查连接有效
            config.setTestWhileIdle(false);// 空闲超时,是否执行检查有效
            config.setTimeBetweenEvictionRunsMillis(60000);// 空闲检查时间
            config.setBlockWhenExhausted(true);// 当连接数耗尽，是否阻塞
        }
        if (jedis == null) {
            Set<HostAndPort> hostAndPorts = new LinkedHashSet<HostAndPort>();
//			String[] nodeArray = nodes.split(",");
//			for (int i = 0; i < nodeArray.length; i++) {
//				String host = nodeArray[i].split(":")[0];
//				int port = Integer.parseInt(nodeArray[i].split(":")[1]);
//				hostAndPorts.add(new HostAndPort(host, port));
//			}
            Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
            nodes.add(new HostAndPort("192.168.3.55", 6379));
            nodes.add(new HostAndPort("192.168.3.55", 6380));
            nodes.add(new HostAndPort("192.168.3.55", 6381));
            nodes.add(new HostAndPort("192.168.3.55", 6382));
            nodes.add(new HostAndPort("192.168.3.55", 6383));
            nodes.add(new HostAndPort("192.168.3.55", 6384));

            jedis = new JedisCluster(nodes, 1000, 1000, 1, "xinleju", config);

        }
        return jedis;
    }


    /**
     * 通过redis获取表主键序列 <br/>
     * 10000以下的数值预留给sql脚本
     *
     * @param tableName
     * @return
     */
    public static String generateKey(String tableName) {
        JedisCluster jedis = getJedis();
        try {
            long key = jedis.incrBy("sequence:" + tableName, 1);
            return String.valueOf(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("generateKey error,{}", tableName);
        } finally {
            // try {
            // //jedis.close();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
        }
        return null;

    }

    public static JedisCluster getResource() {
        JedisCluster jedis = null;
        try {
            jedis = getJedis();
            logger.debug("getResource:{}", jedis);
        } catch (Exception e) {
            logger.error("getResource:{}", e);
            throw e;
        }
        return jedis;
    }

//    public static void main(String[] a) {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        // 最大连接数
//        poolConfig.setMaxTotal(1);
//        // 最大空闲数
//        poolConfig.setMaxIdle(1);
//        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
//        // Could not get a resource from the pool
//        poolConfig.setMaxWaitMillis(1000);
//
//        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
//        nodes.add(new HostAndPort("192.168.3.55", 6379));
//        nodes.add(new HostAndPort("192.168.3.55", 6380));
//        nodes.add(new HostAndPort("192.168.3.55", 6381));
//        nodes.add(new HostAndPort("192.168.3.55", 6382));
//        nodes.add(new HostAndPort("192.168.3.55", 6383));
//        nodes.add(new HostAndPort("192.168.3.55", 6384));
//
//        JedisCluster cluster = new JedisCluster(nodes, 1000, 1000, 1, "xinleju", poolConfig);
//
//
//		Map m=new HashMap();
//		m.put("delflag","aaa");
//
//		String status = cluster.hget("im_user_info","a10697cf09424ceb8d0b91bfa54ef6c2");//cluster.set("delflag:aaa", JacksonUtils.toJson(m));
//		logger.info(status.toString());
////		cluster.expire(Constants.key_prefix_sms + mobile, 60);
//
//
////        String name = cluster.get("delflag:aaa");
////        System.out.println(name);
//        // try {
//        // cluster.close();
//        // } catch (IOException e) {
//        // e.printStackTrace();
//        // }
//        // Map<String, String> s = new HashMap();
//        // s.put("id", "2");
//        // s.put("name", "admin");
//        // s.put("code", "4526111");
//        // s.put("codeUrl", "http://lo/ab.jpg");
//        // JedisUtil.editRow("table:user",s);
//
//        // logger.info(JedisUtil.createSession(s, "13245632542"));
//
//        // JedisUtil.delRow("table:user","1");
//        // logger.info(JedisUtil.generateKey("table_user1"));
//        // JedisUtil.addIndexMobile("mytable", "13254652353", "13");
//        //
//        // logger.info(JedisUtil.fetchValueByKey("index:mytable:mobile","13254652352"));
//    }


}
