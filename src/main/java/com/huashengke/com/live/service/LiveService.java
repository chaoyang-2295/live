package com.huashengke.com.live.service;

import com.huashengke.com.live.body.*;
import com.huashengke.com.live.mapper.LiveMapper;
import com.huashengke.com.tools.cache.LiveCache;
import com.huashengke.com.tools.count.CountCache;
import com.huashengke.com.tools.count.CountCacheProvider;
import com.huashengke.com.tools.count.IdCountType;
import com.huashengke.com.tools.exception.live.LiveErrorRc;
import com.huashengke.com.tools.exception.live.LiveException;
import com.huashengke.com.tools.nim.NIMChatroom;
import com.huashengke.com.tools.nim.NIMPublicService;
import com.huashengke.com.tools.nim.NIMRegisterResponse;
import com.huashengke.com.user.mapper.UserQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class LiveService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private LiveCache liveCache;
    private LiveMapper liveMapper;
    private CountCache liveCounter;
    private CountCache liveRoomCounter;
    private UserQueryMapper userQueryMapper;
    private NIMPublicService nimPublicService;
    private LiveQueryService liveQueryService;
    private AliLiveRequestService aliLiveRequestService;

    @Autowired
    public LiveService(LiveMapper liveMapper, LiveCache liveCache,UserQueryMapper userQueryMapper, NIMPublicService nimPublicService, AliLiveRequestService aliLiveRequestService, LiveQueryService liveQueryService) {

        this.liveCache = liveCache;
        this.liveMapper =liveMapper;
        this.userQueryMapper = userQueryMapper;
        this.nimPublicService = nimPublicService;
        this.liveQueryService = liveQueryService;
        this.aliLiveRequestService = aliLiveRequestService;
        this.liveCounter = CountCacheProvider.getCountCache(IdCountType.live);
        this.liveRoomCounter = CountCacheProvider.getCountCache(IdCountType.liveRoom);
    }

    /**
     * 创建直播间
     *
     */
    public LiveRoom createLiveRoom(LiveRoomCreateBody body) throws Exception {


        if (userQueryMapper.getAutoIdByUserId( body.getUserId() ) == null) {
            throw new Exception("没有这个主播");
        }

        //获取这个主播的网易账号信息，注册网易账号
        NIMRegisterResponse response = nimPublicService.register(body.getUserId());
        if (response.getInfo() == null || response.getCode() != 200) {
            throw new Exception("创建网易账号失败");
        }
        //创建聊天室
        NIMChatroom chatroom = nimPublicService.createChatRoom(response.getInfo().getAccid(), body.getLiveTitle(), "announcement");
        if (chatroom == null || chatroom.getChatroom() == null) {
            throw new Exception("创建直播聊天室失败");
        }
        //直播房间id
        String liveRoomId = liveRoomCounter.getId();
        LiveRoom liveRoom = new LiveRoom(body, liveRoomId, chatroom.getChatroom().getRoomid());
        liveMapper.addLiveRoom(liveRoom);
        return liveRoom;
    }

    /**
     * 创建直播
     */
    public Live createLive(LiveBody liveBody) throws LiveException {

        LiveRoom liveRoom = liveCache.get(liveBody.getLiveRoomId());
        if (liveRoom == null) {
            throw new LiveException("直播间不存在", LiveErrorRc.NoSuchLiveRoom);
        }

        //配置直播转码模板
        aliLiveRequestService.addLiveStreamTranscode(liveBody.getAppName(), "lld");
        aliLiveRequestService.addLiveStreamTranscode(liveBody.getAppName(), "lsd");
        aliLiveRequestService.addLiveStreamTranscode(liveBody.getAppName(), "lhd");

        //开始视频录制
        aliLiveRequestService.startRecordByAppName(liveBody.getAppName());
        //直播id
        String liveId = liveCounter.getId();
        Live live = new Live(liveId, liveBody);

        liveMapper.addLive(live);
        liveMapper.addLiveToLiveRoom( live.getId(), liveRoom.getId() );
        liveMapper.addLiveStream( live.getStreamName(), "MainStream", live.getId(), 0, 1 );
        return live;
    }

    /**
     * 开始直播（视频推流）
     */
    public void startLive(String liveRoomId) throws LiveException {

        liveCache.refresh( liveRoomId );
        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if (live == null) {
            throw new LiveException("当前直播间不存在直播", LiveErrorRc.NoSuchLiveError);
        }
        //开始推流
        aliLiveRequestService.startLiveRequest(live.getAppName(), live.getStreamName());
        //修改直播状态
        liveMapper.updateLiveStatus(live.getId(), LiveStatus.LIVING.name());
        //修改直播流状态
        liveMapper.updateLiveStreamStatus(live.getId(), live.getStreamName(), LiveStreamStatus.Push.getVal());
    }


    /**
     * 停止直播(停止直播流的推送)
     *
     */
    public void stopLive(String liveRoomId) throws LiveException {

        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if (liveRoom.getStatus() == LiveRoomStatus.CLOSE)
            throw new LiveException("该直播间未开启", LiveErrorRc.LiveStatusError);

        for ( LiveStream stream:live.getLiveStreams() ){
            //停止流的推送
            aliLiveRequestService.finishLiveRequest(live.getAppName(), stream.getStream());
            //修改直播流状态
            liveMapper.updateLiveStreamStatus( live.getId(), stream.getStream(), LiveStreamStatus.Stop.getVal() );
        }
        //修改直播状态
        liveMapper.updateLiveStatus(liveRoom.getCurrentLive().getId(), LiveStatus.FINISH.name());
    }

    /**
     * 修改直播间信息
     *
     */
    public void changeLiveRoom(LiveRoomChangeBody liveData) throws LiveException {

        if (liveData == null || liveData.getLiveRoomId() == null) {
            throw new LiveException("请填写直播间ID", LiveErrorRc.NullError);
        }
        //验证该直播间是否存在
        liveCache.get(liveData.getLiveRoomId());
        liveMapper.updateLiveRoom(liveData);
    }

    /**
     * 修改直播状态
     *
     */
    public void changeLiveStatus(String liveRoomId, LiveStatus status) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if (!status.equals(live.getLiveStatus())) {

            if (status.equals(LiveStatus.LIVING) && !live.getLiveStatus().equals(LiveStatus.LIVING)) {
                aliLiveRequestService.startLiveRequest(liveRoom.getCurrentLive().getAppName(), liveRoom.getCurrentLive().getStreamName());
                liveMapper.updateLiveStatus(live.getId(), status.name());
            } else if (!status.equals(LiveStatus.LIVING)) {
                aliLiveRequestService.finishLiveRequest(liveRoom.getCurrentLive().getAppName(), liveRoom.getCurrentLive().getStreamName());
                liveMapper.updateLiveStatus(live.getId(), status.name());
            }
        }
    }

    /**
     * 开始录制,录制流中视频，存储到OSS服务器
     *
     */
    public void startRecord(String liveRoomId) throws LiveException {
        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if (live.isRecord()) {
            throw new LiveException("录制开关已经打开", LiveErrorRc.LiveStatusError);
        }
        //假如改直播间正处于直播状态，则自动切断再重新开始直播
        if (liveRoom.getStatus().equals(LiveRoomStatus.OPEN)) {
            aliLiveRequestService.finishLiveRequest(live.getAppName(), live.getStreamName());
            aliLiveRequestService.startLiveRequest(live.getAppName(), live.getStreamName());
        }
        aliLiveRequestService.startRecordByAppName(live.getAppName());
        liveMapper.changeRecordStatus(live.getId(), 1);
    }

    /**
     * 停止视频录制
     *
     */
    public void stopRecord(String liveRoomId) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if (!live.isRecord()) {
            throw new Exception("录制开关已经关闭");
        }
        aliLiveRequestService.stopLiveRecordByAppName(live.getAppName());
    }

    /**
     * 添加新的直播流
     */
    public void addNewStream(String liveRoomId, String streamName, String desc) throws LiveException {
        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        List<String> streams = live.getLiveStreams().stream().map(liveStream -> streamName).collect(Collectors.toList());
        if(streams.contains(streamName)){
            throw new LiveException("直播流已经存在",LiveErrorRc.LiveStreamAlreadyExistError);
        }
        liveMapper.addNewStream(live.getId(), streamName, desc);
        live.addLiveStream(new LiveStream(streamName, desc, live.getId()));
    }

    /**
     *添加直播流的推送
     */
    public void pushStream(String liveRoomId, String streamName) throws LiveException {

        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();

        aliLiveRequestService.startLiveRequest(live.getAppName(), live.getStreamName());
        liveMapper.updateLiveStreamStatus(live.getId(), streamName, LiveStreamStatus.Push.getVal());
    }

    public void recordCallback(RecordCallbackBody body) {
        try {
            LOGGER.info("ali record request" + body.getEvent());
            String liveId = liveQueryService.getLiveIdByApp(body.getApp());
            if (body.getEvent() == null) {//录制文件生成事件
                liveMapper.addLiveRecord(liveId, new Date(body.getStartTime() * 1000), new Date(body.getStopTime() * 1000), body.getDuration(), "http://qzlivevideo.oss-cn-shanghai.aliyuncs.com/" + body.getUri());
            } else {
                liveCache.get(liveId);
                int status = 0;
                if (body.getEvent().equals("record_resumed") || body.getEvent().equals("record_started")) {
                    status = 1;
                }
                liveMapper.changeRecordStatus(liveId, status);
            }
        } catch (Exception e) {
            LOGGER.error("ali record callback error：", e);
        }
    }
}

