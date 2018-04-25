package com.huashengke.com.tools.cache;

import com.huashengke.com.live.body.UserInfo;
import com.huashengke.com.tools.ObjectSerializer;
import com.huashengke.com.tools.redis.JedisBusiness;
import com.huashengke.com.tools.redis.JedisService;
import com.huashengke.com.user.dao.NewUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;

@Service
public class UserCache implements HuaShengKeCache {

    private JedisService jedisService;
    @Autowired
    private NewUserDao userDao;

    public UserCache() {
        this.jedisService = new JedisService();
    }

    /**
     * 通过用户id从缓存中获取用戶信息
     * @param userId
     * @return
     */
    public UserInfo get(String userId) throws Exception {
        UserInfo userInfo = jedisService.doJedisOperation((jedis, key) -> {
            //获取用户信息对象
            String userInfoStr = jedis.get(key);
            //将该对象反序列换返回
            return ObjectSerializer.instance().deserialize(userInfoStr,
                    UserInfo.class);
        }, JedisBusiness.User, userId);
        if (userInfo == null) {
            //重置缓存中的该用户的数据
            userInfo = resetUser(userId);
            if (userInfo == null) {
                throw new Exception("no such user, userId:" + userId);
            }
        }
        return userInfo;
    }

    /**
     * 重置缓存中用户的信息
     */
    private UserInfo resetUser(String userId) {
        //获取用户信息
        UserInfo userInfo = userDao.getUserInfo( userId );
        if (userInfo == null) {
            return null;
        }
        //将用户信息保存到缓存中
        jedisService.doJedisOperationAsyn((jedis, key) -> {
                    Pipeline pipeline = jedis.pipelined();
                    //将userInfo序列化保存到缓存内存中
                    pipeline.set(key, ObjectSerializer.instance().serialize(userInfo));
                    //设置过期时间
                    pipeline.expire(key,60*30);
                    return pipeline.syncAndReturnAll();
                },
                JedisBusiness.User, userId);
        return userInfo;
    }

    /**
     * 刷新缓存
     * @param id
     */
    @Override
    public void refresh(String id) {
        jedisService.doJedisOperation( (jedis,key)->jedis.del(key),
                JedisBusiness.Live,id);
    }
}
