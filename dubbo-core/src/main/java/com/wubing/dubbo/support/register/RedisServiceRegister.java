package com.wubing.dubbo.support.register;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis 注册服务注册中心中心
 *
 * @author: WB
 * @version: v1.0
 */
public class RedisServiceRegister {
    /**
     * Redis注册中心地址
     */
    private String host;
    /**
     * Redis注册中心端口
     */
    private int port;
    /**
     * Redis 连接池
     */
    private static JedisPool jedisPool;

    public RedisServiceRegister(String host, int port) {
        this.host = host;
        this.port = port;
        this.jedisPool = new JedisPool(host, port);
    }

    /**
     * 获取远程服务
     *
     * @param serverName 服务名称
     * @return
     */
    public static String getServerAddress(String serverName) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            return resource.get(serverName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
        return null;
    }

    /**
     * 注册服务
     *
     * @param serverName    服务名称
     * @param serverAddress 服务地址
     */
    public static void registerServer(String serverName, String serverAddress) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            resource.set(serverName, serverAddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }

}
