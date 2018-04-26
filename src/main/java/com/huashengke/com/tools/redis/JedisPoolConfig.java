package com.huashengke.com.tools.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * redis 的连接池配置信息
 */
public class JedisPoolConfig {

    private static JedisPool jedisPool;

    /**
     * 创建redis连接池
     * @return  JedisPool
     */
    public static JedisPool buildPool(){
        if(jedisPool==null) {
            jedisPool = build();
        }
        return jedisPool;
    }

    private static JedisPool build(){

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream( "jedis.properties" );
        Properties countProperties = new Properties();
        GenericObjectPoolConfig poolConfig = null;
        try {
            countProperties.load( in );
            poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMinIdle(Integer.parseInt(countProperties.getProperty("redis.pool.minIdle")));
            poolConfig.setMaxTotal(Integer.parseInt(countProperties.getProperty("redis.pool.maxTotal")));
            poolConfig.setMaxIdle(Integer.parseInt(countProperties.getProperty("redis.pool.maxIdle")));
            poolConfig.setMaxWaitMillis(Integer.parseInt(countProperties.getProperty("redis.pool.maxWaitMillis")));
            poolConfig.setTestOnReturn(Boolean.parseBoolean(countProperties.getProperty("redis.pool.testOnReturn")));
            poolConfig.setTestOnBorrow(Boolean.parseBoolean(countProperties.getProperty("redis.pool.testOnBorrow")));

        }catch (IOException e){
            e.printStackTrace();
        }
        return new JedisPool(poolConfig, countProperties.getProperty("redis.host"), Integer.parseInt(countProperties.getProperty("redis.port")),
                Integer.parseInt(countProperties.getProperty("redis.timeout")), countProperties.getProperty("redis.password"), Integer.parseInt(countProperties.getProperty("redis.database")));
    }
}
