package com.huashengke.com.tools.redis;

/**
 * 业务类型
 */
public enum  JedisBusiness {

    /**
     * 用来保存自增id的计数器
     */
    IdCount(JedisModule.C,"IC"),
    /**
     * 用来保存用户缓存
     */
    User(JedisModule.U,"U"),
    /**
     * 用来保存直播缓存
     */
    LiveRoom(JedisModule.L,"L"),

    /**
     * 用于保存直播在线人数缓存
     */
    LiveOnlineNumber(JedisModule.L,"NUM");

    private JedisModule jedisModule;

    private String shortName;

    public String getShortName() {

        return shortName;
    }

    JedisBusiness(JedisModule jedisModule, String shortName) {

        this.jedisModule = jedisModule;
        this.shortName = shortName;
    }

    public JedisModule getJedisModule() {
        return jedisModule;
    }
}
