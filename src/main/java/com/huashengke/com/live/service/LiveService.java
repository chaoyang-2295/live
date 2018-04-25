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
import com.huashengke.com.tools.exception.user.NoSuchUserException;
import com.huashengke.com.tools.exception.user.UserErrorRc;
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

    /**
     * 直播ID自动计数器
     */
    private CountCache liveCounter;
    /**
     * 直播流ID自动计数器
     */
    private CountCache streamCounter;
    /**
     * 直播室ID自动计数器
     */
    private CountCache liveRoomCounter;

    @Autowired
    private NIMPublicService nimPublicService;
    @Autowired
    private AliLiveRequestService aliLiveService;
    @Autowired
    private LiveCache liveCache;
    @Autowired
    private LiveQueryService queryService;
    @Autowired
    private NewLiveDao liveDao;

    public LiveService() {
        this.liveCounter = CountCacheProvider.getCountCache(IdCountType.live);
        this.streamCounter = CountCacheProvider.getCountCache(IdCountType.stream);
        this.liveRoomCounter = CountCacheProvider.getCountCache(IdCountType.liveRoom);
    }

    /**
     * 创建直播间
     *
     * @param liveRoomCreateBody
     */
    public LiveRoom createLiveRoom(LiveRoomCreateBody liveRoomCreateBody) throws Exception {

        //    confirmLiveTextFormat(liveRoomCreateBody);

        if (!queryService.isExistUserId(liveRoomCreateBody.getUserId())) {
            throw new NoSuchUserException("没有这个主播", UserErrorRc.NoSuchUserError);
        }
        //获取这个主播的网易账号信息，注册网易账号
        NIMUser nimUserInfo = queryService.getNimUserInfo(liveRoomCreateBody.getUserId());
        NIMRegisterResponse response = nimPublicService.register(nimUserInfo);
        if (response.getInfo() == null || response.getCode() != 200) {
            throw new Exception("创建网易账号失败");
        }
        //创建聊天室
        NIMChatroom chatroom = nimPublicService.createChatroom(response.getInfo().getAccid(), liveRoomCreateBody.getLiveTitle(), "announcement", "", "ext");
        if (chatroom == null || chatroom.getChatroom() == null) {
            throw new Exception("创建直播聊天室失败");
        }
        //直播房间id
        String liveRoomId = liveRoomCounter.getId();
        LiveRoom liveRoom = new LiveRoom(liveRoomCreateBody, liveRoomId, chatroom.getChatroom().getRoomid());
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
        aliLiveService.addLiveStreamTranscode(liveBody.getAppName(), "lld");
        aliLiveService.addLiveStreamTranscode(liveBody.getAppName(), "lsd");
        aliLiveService.addLiveStreamTranscode(liveBody.getAppName(), "lhd");

        //开始视频录制
        aliLiveService.startRecordByAppName(liveBody.getAppName());
        //直播id
        String liveId = liveCounter.getId();
        Live live = new Live(liveId, liveBody);

        liveDao.addLive(live);
        return live;
    }

    /**
     * 开始直播（视频推流）
     */
    public void startLive(String liveRoomId) throws LiveException {

        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if (liveRoom.getStatus() != LiveRoomStatus.CLOSE) {
            throw new LiveException("当前直播间未开启,不能进行直播！", LiveErrorRc.LiveStatusError);
        }
        if (live == null) {
            throw new LiveException("当前直播间不存在直播", LiveErrorRc.NoSuchLiveError);
        }
        //开始推流
        aliLiveService.startLiveRequest(live.getAppName(), live.getStreamName());
        //修改直播状态
        liveDao.changeLiveStatus(live, LiveStatus.LIVING);
        //修改直播流状态
        liveDao.changeLiveStreamStatus(live.getId(), LiveStreamStatus.Push);
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
        aliLiveService.finishLiveRequest(liveRoom.getCurrentLive().getAppName(), liveRoom.getCurrentLive().getStreamName());
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
    public void changeLiveStatus(String liveId, LiveStatus status) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if (!status.equals(liveRoom.getStatus())) {

            if (status.equals(LiveRoomStatus.OPEN) && !liveRoom.getStatus().equals(LiveRoomStatus.OPEN)) {
                aliLiveService.startLiveRequest(liveRoom.getCurrentLive().getAppName(), liveRoom.getCurrentLive().getStreamName());
                liveDao.changeLiveStatus(liveRoom.getCurrentLive(), status);
            } else if (!status.equals(LiveRoomStatus.OPEN)) {
                aliLiveService.finishLiveRequest(liveRoom.getCurrentLive().getAppName(), liveRoom.getCurrentLive().getStreamName());
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
        DescribeLiveStreamTranscodeInfoResponse queryResponse = aliLiveService.getLiveMixConfig();
        String[] modifiedDefinitions = definition.split("_");


        //获取目前拥有的转码
        List<String> nowDefinitions = queryResponse.getDomainTranscodeList().parallelStream()
                .filter(transcodeInfo -> transcodeInfo.getTranscodeApp().equals(live.getAppName()))
                .map(transcodeInfo -> transcodeInfo.getTranscodeTemplate())
                .collect(Collectors.toList());
        //添加混流转码
        nowDefinitions.addAll(aliLiveService.getMixConfigByAppName(live.getAppName()));
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
                aliLiveService.deleteLiveMixConfig(live.getAppName());
            } else {
                aliLiveService.deleteLiveStreamTranscode(live.getAppName(), nowDefinitions.get(i));
            }
        }
        for (int i = 0; i < addDefinitions.size(); i++) {
            String addDefinition = addDefinitions.get(i);
            if (addDefinition != null && addDefinition.charAt(0) == 'm') {
                aliLiveService.addLiveMixConfig(live.getAppName(), addDefinition);
            } else {
                aliLiveService.addLiveStreamTranscode(live.getAppName(), addDefinition);
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
            aliLiveService.finishLiveRequest(live.getAppName(), live.getStreamName());
            aliLiveService.startLiveRequest(live.getAppName(), live.getStreamName());
        }
        aliLiveService.startRecordByAppName(live.getAppName());
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
        aliLiveService.stopLiveRecordByAppName(live.getAppName());
    }

    public void recordCallback(RecordCallbackBody body) {
        try {
            LOGGER.info("ali record request" + body.getEvent());
            String liveId = queryService.getLiveIdByApp(body.getApp());
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

