package com.huashengke.com.tools.count;


/**
 * 根据传入的类型，获得对应的计数缓存
 */
public class CountCacheProvider {

    public static CountCache getCountCache(IdCountType idCountType){

        return new CountCache( idCountType );
    }
}
