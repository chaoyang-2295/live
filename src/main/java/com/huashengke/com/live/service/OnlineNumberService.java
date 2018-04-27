package com.huashengke.com.live.service;


import com.huashengke.com.live.body.LiveRoom;
import com.huashengke.com.live.dao.NewLiveDao;
import com.huashengke.com.tools.exception.live.LiveException;
import com.huashengke.com.tools.nim.NIMChatRoomData;
import com.huashengke.com.tools.nim.NIMService;
import com.huashengke.com.tools.redis.JedisBusiness;
import com.huashengke.com.tools.redis.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;

/**
 * Created by chentz on 2017/12/6.
 */
@Service
public class OnlineNumberService {
    private static Logger logger = LoggerFactory.getLogger(OnlineNumberService.class);
    private NIMService nimManagerService;
    private JedisService jedisService;
    @Autowired
    private NewLiveDao liveDao;
    //上一次在线人数更新的时间

    private static final long UPDATE_GAP = 1000 * 30;

    @Autowired
    public OnlineNumberService() {
        nimManagerService = new NIMService();
        jedisService = new JedisService();
    }

    public int getOnlineNumber(LiveRoom liveRoom) throws LiveException {
        String onlineNumber = jedisService.doJedisOperation((jedis, key) -> jedis.get(key), JedisBusiness.LiveOnlineNumber, liveRoom.getId());
        return onlineNumber == null ? updateOnlineNumber(liveRoom) : Integer.parseInt(onlineNumber);
    }

    public int updateOnlineNumber(LiveRoom liveRoom) throws LiveException {
        NIMChatRoomData chatRoomData = nimManagerService.getNIMRoomData( Integer.parseInt(liveRoom.getChatRoomId()));
        Integer number;
        if (chatRoomData != null) {
            number = chatRoomData.getOnlineusercount();
            int maxOnlineNumber = liveRoom.getCurrentLive().getMaxOnlineNumber();
            if (maxOnlineNumber >= 0 && number > maxOnlineNumber)
                liveDao.changeMaxOnlineNumber(liveRoom.getId(), liveRoom.getCurrentLive().getId(), number);
        }else {
            number = 0;
        }
        jedisService.doJedisOperation((jedis, key) -> {
            Pipeline pipeline = jedis.pipelined();
            pipeline.set(key, number.toString());
            pipeline.expire(key, 60);
            pipeline.sync();
            return 0;
        }, JedisBusiness.LiveOnlineNumber, liveRoom.getId());
        return number;
    }
}
