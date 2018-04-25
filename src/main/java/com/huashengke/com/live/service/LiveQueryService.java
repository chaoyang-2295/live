package com.huashengke.com.live.service;

import com.huashengke.com.live.body.*;
import com.huashengke.com.tools.JoinOn;
import com.huashengke.com.tools.QueryUtil;
import com.huashengke.com.tools.StringUtil;
import com.huashengke.com.tools.cache.LiveCache;
import com.huashengke.com.tools.exception.live.LiveErrorRc;
import com.huashengke.com.tools.exception.live.LiveException;
import com.huashengke.com.tools.nim.NIMUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;


@Service
public class LiveQueryService extends QueryUtil {

    @Autowired
    private LiveCache liveCache;

    @Autowired
    private OnlineNumberService onlineNumberService;

    @Autowired
    private AliLiveRequestService aliLiveRequestService;

    @Autowired
    protected LiveQueryService(@Qualifier("dataSource") DataSource aDataSource) {
        super(aDataSource);
    }

    public boolean isExistApp(String app){
        String result = queryString("select id from tbl_vw_new_live where app=?",app);
        return !StringUtil.isStringEmpty(result);
    }

    public boolean isExistUserId(String userId){
        String result = queryString("select id from tbl_vw_user where user_id=?",userId);
        return !StringUtil.isStringEmpty(result);
    }

    public boolean isExistVideo(String videoId){
        String result = queryString("select id from tbl_vw_video where video_id=?",videoId);
        return !StringUtil.isStringEmpty(result);
    }

    public LiveStream getLiveStream(String liveId, String stream) throws LiveException {
        LiveStream liveStream = this.queryObject(LiveStream.class,
                "select * from tbl_vw_live_stream where live_id = ? and stream = ?",
                new JoinOn(), liveId, stream);
        if (liveStream == null) {
            throw new LiveException("该直播间找不到流%s"+stream, LiveErrorRc.NoSuchLiveStreamError);
        }
        return liveStream;
    }

    public LiveStream getLiveStreamByAppName(String appName, String streamName) throws LiveException {
        LiveStream roomStream = queryObject(LiveStream.class,
                "select s.* from tbl_vw_new_live l LEFT JOIN tbl_vw_live_stream s on l.id=s.live_id WHERE l.app = ? and s.stream = ?",
                new JoinOn(), appName, streamName);
        if (roomStream == null) {
            throw new LiveException("找不到该流appName:"+appName+",streamName:"+streamName,LiveErrorRc.NoSuchLiveStreamError);
        }
        return roomStream;
    }

    public String getLiveIdByApp(String app) throws LiveException {
        String liveId = queryString("select id from tbl_vw_new_live where app=?",app);
        if(StringUtil.isStringEmpty(liveId)){
            throw new LiveException("no such live,app:"+app,LiveErrorRc.NoSuchLiveError);
        }
        return liveId;
    }

    public boolean isExistLiveStream(String liveId,String stream){
        return queryString("select id from tbl_vw_live_stream where live_id = ? and stream = ?",liveId, stream)!=null;
    }

    public LiveContentBody getLiveContentBody(String liveId, String userId) throws LiveException {
        LiveRoom liveRoom = liveCache.get(liveId);
        return new LiveContentBody(liveRoom,userId);
    }

    public LivePlayBody getLivePlayBody(String liveId, String userId, MediaType mediaType) throws LiveException {
        LiveRoom liveRoom = liveCache.getShowLive(liveId);
        Live live = liveRoom.getCurrentLive();
        String url = aliLiveRequestService.getUrl(live.getAppName(), live.getStreamName());
        int onlineNumber = onlineNumberService.getOnlineNumber(liveRoom);
        String maxDefinition = liveRoom.getDefinitions().get(liveRoom.getDefinitions().size()-1).getType();
        return new LivePlayBody(liveRoom,new LiveContentBody(liveRoom,userId),onlineNumber,url,aliLiveRequestService.getLiveAuthenticationKey(live.getAppName(), live.getStreamName(),mediaType,maxDefinition));
    }

    public NIMUser getNimUserInfo(String userId) {
        return queryObject(NIMUser.class, "SELECT `id` auto_id,`user_id`,case `cert_allow_public` when 1 then `cert_realname` else `realname` end name,`avatar`,`nim_accid` accid,`nim_token` token FROM `tbl_vw_user` WHERE `user_id`=?", new JoinOn(), userId);
    }

    public LiveStatusData getLiveStatusData(String liveId, MediaType mediaType, Definition definition) throws LiveException {
        LiveRoom liveRoom = liveCache.get(liveId);
        Live live = liveRoom.getCurrentLive();
        int onlineNumber = onlineNumberService.getOnlineNumber(liveRoom);
        String url = aliLiveRequestService.getUrl(live.getAppName(), live.getStreamName());
        return new LiveStatusData(url, liveRoom,onlineNumber,aliLiveRequestService.getLiveAuthenticationKey(live.getAppName(), live.getStreamName(),mediaType,
                definition==null? liveRoom.getDefinitions().get(liveRoom.getDefinitions().size()-1).getType():definition.name()));
    }

}