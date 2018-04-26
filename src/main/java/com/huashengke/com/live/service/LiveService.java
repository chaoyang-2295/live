package com.huashengke.com.live.service;

import com.aliyuncs.live.model.v20161101.DescribeLiveStreamTranscodeInfoResponse;
import com.huashengke.com.live.body.*;
import com.huashengke.com.live.dao.NewLiveDao;
import com.huashengke.com.tools.cache.LiveCache;
import com.huashengke.com.tools.count.CountCache;
import com.huashengke.com.tools.count.CountCacheProvider;
import com.huashengke.com.tools.count.IdCountType;
import com.huashengke.com.tools.exception.live.LiveErrorRc;
import com.huashengke.com.tools.exception.live.LiveException;
import com.huashengke.com.tools.nim.NIMChatroom;
import com.huashengke.com.tools.nim.NIMPublicService;
import com.huashengke.com.tools.nim.NIMRegisterResponse;
import com.huashengke.com.tools.nim.NIMUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LiveService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private NewLiveDao liveDao;
    private LiveCache liveCache;
    private CountCache liveCounter;
    private CountCache liveRoomCounter;
    private NIMPublicService nimPublicService;
    private LiveQueryService liveQueryService;
    private AliLiveRequestService aliLiveRequestService;

    @Autowired
    public LiveService(NewLiveDao liveDao, LiveCache liveCache, NIMPublicService nimPublicService, AliLiveRequestService aliLiveRequestService, LiveQueryService liveQueryService) {

        this.liveDao = liveDao;
        this.liveCache = liveCache;
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

        if (!liveQueryService.isExistUserId(body.getUserId())) {
            throw new Exception("没有这个主播");
        }

        //获取这个主播的网易账号信息，注册网易账号
        NIMUser nimUserInfo = liveQueryService.getNimUserInfo(body.getUserId());
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
        liveDao.addLiveRoom(liveRoom);
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

        liveDao.addLive(live);
        return live;
    }

    /**
     * 开始直播（视频推流）
     */
    public String startLive(String liveRoomId) throws LiveException {

        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if (liveRoom.getStatus() != LiveRoomStatus.CLOSE) {
            throw new LiveException("当前直播间未开启,不能进行直播！", LiveErrorRc.LiveStatusError);
        }
        if (live == null) {
            throw new LiveException("当前直播间不存在直播", LiveErrorRc.NoSuchLiveError);
        }
        //开始推流
        aliLiveRequestService.startLiveRequest(live.getAppName(), live.getStreamName());
        //修改直播状态
        liveDao.changeLiveStatus(live, LiveStatus.LIVING);
        //修改直播流状态
        liveDao.changeLiveStreamStatus(live.getId(), live.getStreamName(), LiveStreamStatus.Push);

        return aliLiveRequestService.generateAuthKey(live.getAppName(), live.getStreamName());
    }


    /**
     * 停止直播(停止直播流的推送)
     *
     */
    public void stopLive(String liveRoomId) throws LiveException {

        LiveRoom liveRoom = liveCache.get(liveRoomId);
        if (liveRoom == null)
            throw new LiveException("该直播间不存在", LiveErrorRc.NoSuchLiveRoom);
        if (liveRoom.getStatus() != LiveRoomStatus.OPEN)
            throw new LiveException("该直播间未开启", LiveErrorRc.LiveStatusError);
        //停止流的推送
        aliLiveRequestService.finishLiveRequest(liveRoom.getCurrentLive().getAppName(), liveRoom.getCurrentLive().getStreamName());
        //修改直播状态
        liveDao.changeLiveStatus(liveRoom.getCurrentLive(), LiveStatus.FINISH);
    }

    /**
     * 修改直播间信息
     *
     */
    public void changeLiveRoom(LiveRoomChangeBody liveData) throws LiveException {

        if (liveData == null || liveData.getLiveRoomId() == null) {
            throw new LiveException("请填写直播间ID", LiveErrorRc.NullError);
        }
        //验证该直播是否存在
        liveCache.get(liveData.getLiveRoomId());

        liveDao.changeLiveRoom(liveData);
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
                liveDao.changeLiveStatus(liveRoom.getCurrentLive(), status);
            } else if (!status.equals(LiveStatus.LIVING)) {
                aliLiveRequestService.finishLiveRequest(liveRoom.getCurrentLive().getAppName(), liveRoom.getCurrentLive().getStreamName());
                liveDao.changeLiveStatus(liveRoom.getCurrentLive(), status);
            }
        }
    }

    /**
     * 转码配置  将当前的转码配置替换为提供的转码模板
     *
     */
    public void transCodeConfig(String definition, String liveRoomId) throws LiveException {
        if (definition.charAt(0) == 'm' && definition.length() != 3)
            throw new LiveException("混流转码只能配置一种模板",LiveErrorRc.LiveCustomeError);
        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if (liveRoom.getStatus().equals(LiveRoomStatus.OPEN))
            throw new LiveException("直播过程中无法更改转码配置",LiveErrorRc.LiveCustomeError);
        //查询出直播转码的所有数据
        DescribeLiveStreamTranscodeInfoResponse queryResponse = aliLiveRequestService.getLiveMixConfig();
        String[] modifiedDefinitions = definition.split("_");


        //获取目前拥有的转码
        List<String> nowDefinitions = queryResponse.getDomainTranscodeList().parallelStream()
                .filter(transcodeInfo -> transcodeInfo.getTranscodeApp().equals(live.getAppName()))
                .map(transcodeInfo -> transcodeInfo.getTranscodeTemplate())
                .collect(Collectors.toList());
        //添加混流转码
        nowDefinitions.addAll(aliLiveRequestService.getMixConfigByAppName(live.getAppName()));
        //需要添加的转码
        List<String> addDefinitions = new ArrayList<>();
        for (int i = 0; i < modifiedDefinitions.length; i++) {
            if (nowDefinitions.contains(modifiedDefinitions[i])) {
                nowDefinitions.remove(modifiedDefinitions[i]);//转化为需要删除的转码
            } else {
                addDefinitions.add(modifiedDefinitions[i]);
            }
        }
        for (int i = 0; i < nowDefinitions.size(); i++) {
            String deleteDefinition = nowDefinitions.get(i);
            if (deleteDefinition.charAt(0) == 'm') {
                aliLiveRequestService.deleteLiveMixConfig(live.getAppName());
            } else {
                aliLiveRequestService.deleteLiveStreamTranscode(live.getAppName(), nowDefinitions.get(i));
            }
        }
        for (int i = 0; i < addDefinitions.size(); i++) {
            String addDefinition = addDefinitions.get(i);
            if (addDefinition != null && addDefinition.charAt(0) == 'm') {
                aliLiveRequestService.addLiveMixConfig(live.getAppName(), addDefinition);
            } else {
                aliLiveRequestService.addLiveStreamTranscode(live.getAppName(), addDefinition);
            }
        }
        liveDao.changeDefinition(liveRoomId, definition);
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
        liveDao.changeRecordStatus(live.getId(), 1);
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
        liveDao.addNewStream(live.getId(), streamName, desc);
        live.addLiveStream(new LiveStream(streamName, desc, live.getId()));
    }

    /**
     *添加直播流的推送
     */
    public void pushStream(String liveRoomId, String streamName) throws LiveException {

        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();

        aliLiveRequestService.startLiveRequest(live.getAppName(), live.getStreamName());

        liveDao.changeLiveStreamStatus(live.getId(), streamName, LiveStreamStatus.Push);
    }

    public void recordCallback(RecordCallbackBody body) {
        try {
            LOGGER.info("ali record request" + body.getEvent());
            String liveId = liveQueryService.getLiveIdByApp(body.getApp());
            if (body.getEvent() == null) {//录制文件生成事件
                liveDao.addRecordVideo(liveId, new Date(body.getStartTime() * 1000), new Date(body.getStopTime() * 1000), body.getDuration(), "http://qzlivevideo.oss-cn-shanghai.aliyuncs.com/" + body.getUri());
            } else {
                liveCache.get(liveId);
                int status = 0;
                if (body.getEvent().equals("record_resumed") || body.getEvent().equals("record_started")) {
                    status = 1;
                }
                liveDao.changeRecordStatus(liveId, status);
            }
        } catch (Exception e) {
            LOGGER.error("ali record callback error：", e);
        }
    }
}

