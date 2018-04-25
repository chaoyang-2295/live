package com.huashengke.com.tools.redis;


/**
 * redis key 构建器
 */
public class JedisKeyBuilder {

    private static String SEPERATOR = ":";

    /**
     * 根据业务类型和参数，计算出redis存储的key值
     * @param business  业务类型
     * @param args     参数
     * @return         key
     */
    public static String keyBuilder(JedisBusiness business,String... args){
        StringBuilder sb = new StringBuilder(business.getJedisModule().name());
        sb.append(SEPERATOR).append(business.getShortName());
        for (int i = 0; i < args.length; i++) {
            sb.append(SEPERATOR).append(args[i]);
        }
        return sb.toString();
    }
}
