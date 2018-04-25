package com.huashengke.com.tools.redis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public class JedisService {

    public JedisService() {
    }

    /**
     * 根据传入的业务类型和参数生成key,
     * 获取redis连接对象
     * 调用将redis和key传入回调函数，并调用该函数
     * @param jedisAction 回调函数的接口
     * @param business    业务类型
     * @param args        参数
     * @param <T>
     * @return            回调函数的返回值
     */
    public <T> T doJedisOperation(JedisKeyAction<T> jedisAction, JedisBusiness business, String... args){
        String key = JedisKeyBuilder.keyBuilder(business,args);
        try(Jedis jedis = JedisPoolConfig.buildPool().getResource()) {
            return jedisAction.doAction(jedis, key);
        }
    }

    public <T> T doJedisOperation(JedisAction<T> jedisAction, JedisBusiness business){
        try(Jedis jedis = JedisPoolConfig.buildPool().getResource()) {
            return jedisAction.doAction(jedis);
        }
    }

    public <T> CompletableFuture<T> doJedisOperationAsyn(JedisKeyAction<T> jedisAction, JedisBusiness business, String... args){
        return CompletableFuture.supplyAsync(()->{
            String key = JedisKeyBuilder.keyBuilder(business,args);
            try(Jedis jedis = JedisPoolConfig.buildPool().getResource()) {
                return jedisAction.doAction(jedis, key);
            }
        });
    }

}
