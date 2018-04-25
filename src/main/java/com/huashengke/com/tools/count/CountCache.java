package com.huashengke.com.tools.count;


import com.huashengke.com.tools.redis.JedisBusiness;
import com.huashengke.com.tools.redis.JedisService;

public class CountCache {

    private final static String DEFAULT_COUNTER_PREFIX = "COUNT_";

    /**redis中id对应的键*/
    private String key;
    private IdCountType idCountType;
    private JedisService jedisService;

    public CountCache(IdCountType idCountType) {

        this.idCountType = idCountType;

        this.key = buildCounterKey( idCountType.name()  );
        jedisService = new JedisService();
        if (idCountType.getDefaultNumber()  > 0)
            //根据传入的默认数量作为种子，产生id
            checkInitializeSeed( idCountType.getDefaultNumber() );
    }

    /**
     * 返回自生成的id
     * @return
     */
    public String getId(){
        return  idCountType.getJoinString()+ idCountType.getDefaultNumber() + incValue();
    }

    /**
     * 根据缓存中的键，将键的整数值递增一，然后返回
     * @return 增加后的键的值
     */
    private long incValue() {
        return jedisService.doJedisOperation( (jedis, key1) -> jedis.incr(key1),
                JedisBusiness.IdCount,key);
    }
    /**
     * 根据id类型加上默认前缀做为redis的键
     * */
    private String buildCounterKey(String counterName) {
        return DEFAULT_COUNTER_PREFIX + counterName;
    }

    /**
     * 根据传入的种子，在redis中初始化id
     * @param seed
     */
    private void checkInitializeSeed(long seed) {
        jedisService.doJedisOperation( (jedis, key1) -> {
            String value = jedis.get(key);
            if (value == null) {
                jedis.set(key, String.valueOf(seed));
            } else if (Long.valueOf(value) <= seed) {
                jedis.set(key, String.valueOf(seed));
            }
            return 0;
        },JedisBusiness.IdCount,key );
    }
}
