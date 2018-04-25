package com.huashengke.com.tools.redis;

import redis.clients.jedis.Jedis;


public interface JedisKeyAction<T> {
    T doAction(Jedis jedis, String key);
}
