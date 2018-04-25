package com.huashengke.com.tools.redis;

/**
 * 业务类型
 */
public enum  JedisBusiness {
    /**
     * 普通的内容计数 (使用hash保存)
     */
    Count(JedisModule.C,"C"),

    /**
     * 能够过时的计数器 (使用普通的key值保存)
     */
    ExpireCount(JedisModule.C,"EC"),

    /**
     * 用来保存自增id的计数器
     */
    IdCount(JedisModule.C,"IC"),

    /**
     * 用来保存用户缓存
     */
    User(JedisModule.U,"U"),

    /**
     * 用户缓存
     */
    UserFollow(JedisModule.U,"F"),

    /**
     * 用来保存用户的临时key值如修改密码时邮件中的key
     */
    UserKey(JedisModule.U,"UK"),

    /**
     * 用来保存直播缓存
     */
    Live(JedisModule.L,"L"),

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
