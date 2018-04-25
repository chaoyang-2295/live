package com.huashengke.com.tools.redis;

import redis.clients.jedis.Jedis;

public interface JedisAction<T> {
    T doAction(Jedis jedis);
}
