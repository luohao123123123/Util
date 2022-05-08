package com.Utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Closeable;
import java.io.IOException;

/**
 * 实现Closeable接口，
 */
public class RedisUtil implements Closeable {
    private static JedisPool pool = null;



    public RedisUtil() {
    }

    /**
     * 连接池
     * @return
     */
    public static JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(100);  //最大连接数
            config.setMaxIdle(5);    //允许最大连接空闲数
            pool = new JedisPool(config, "192.168.213.60", 6379, 2000,"root",0);
        }

        return pool;
    }


    /**
     * 连接
     * @return
     */
    public static Jedis getConn() {
        return getPool().getResource();
    }

    /**
     * 关流
     * @throws IOException
     */
    public void close() throws IOException {
        pool.close();

    }
}
