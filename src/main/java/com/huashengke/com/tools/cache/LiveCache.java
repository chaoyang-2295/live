package com.huashengke.com.tools.cache;


import com.huashengke.com.live.body.LiveRoom;
import com.huashengke.com.live.body.LiveRoomStatus;
import com.huashengke.com.live.dao.NewLiveDao;
import com.huashengke.com.tools.ObjectSerializer;
import com.huashengke.com.tools.StringUtil;
import com.huashengke.com.tools.exception.live.LiveErrorRc;
import com.huashengke.com.tools.exception.live.LiveException;
import com.huashengke.com.tools.exception.live.NoSuchLiveException;
import com.huashengke.com.tools.redis.JedisBusiness;
import com.huashengke.com.tools.redis.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;

@Service
public class LiveCache implements HuaShengKeCache {

    private JedisService jedisService;

    @Autowired
    private NewLiveDao liveDao;

    @Autowired
    public LiveCache() {
        this.jedisService = new JedisService();
    }

    /**
     * 通过直播id获取直播
     * @param liveId  直播id
     * @return        直播对象
     * @throws Exception
     */
    public LiveRoom get(String liveId) throws LiveException {
        LiveRoom liveRoom = jedisService.doJedisOperation((jedis, key) -> {
            String liveStr = jedis.get(key);
            return ObjectSerializer.instance().deserialize(liveStr,
                    LiveRoom.class);
        }, JedisBusiness.Live, liveId);
        if (liveRoom == null) {
            liveRoom = resetLive(liveId);
            if (liveRoom == null) {
                throw new NoSuchLiveException("直播不存在", LiveErrorRc.NoSuchLiveError);
            }
        }
        return liveRoom;
    }

    /**
     * 获取当前直播信息
     * @param liveId
     * @return
     * @throws LiveException
     */
    public LiveRoom getShowLive(String liveId) throws LiveException {
        LiveRoom liveRoom = get(liveId);
        if (StringUtil.isStringEmpty(liveRoom.getCurrentLive().getId()) && !liveRoom.getStatus().equals(LiveRoomStatus.CLOSE)) {
            throw new LiveException("直播不存在", LiveErrorRc.NoSuchLiveError);
        }
        return liveRoom;
    }

    /**
     * 重置直播对象
     * @param
     * @return
     */
    public LiveRoom resetLive(String liveRoomId) {

        LiveRoom liveRoom = liveDao.getLive(liveRoomId);
        if (liveRoom == null) {
            return null;
        }
        jedisService.doJedisOperation((jedis, key) -> {
            Pipeline pipeline = jedis.pipelined();
            pipeline.set(key, ObjectSerializer.instance().serialize(liveRoom));
            pipeline.expire(key,60*30);
            return pipeline.syncAndReturnAll();
        }, JedisBusiness.Live, liveRoomId);
        return liveRoom;
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
