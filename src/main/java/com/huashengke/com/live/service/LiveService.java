package com.huashengke.com.live.service;

import com.aliyuncs.live.model.v20161101.DescribeLiveStreamTranscodeInfoResponse;
import com.huashengke.com.live.body.*;
import com.huashengke.com.live.dao.NewLiveDao;
import com.huashengke.com.live.mapper.LiveMapper;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class LiveService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**直播ID自动计数器*/
    private CountCache liveCounter;
    /**直播流ID自动计数器*/
    private CountCache streamCounter;
    /**直播室ID自动计数器*/
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
     * @param liveRoomCreateBody
     * @return
     * @throws Exception
     */
    public LiveRoom createLiveRoom(LiveRoomCreateBody liveRoomCreateBody) throws Exception {

    //    confirmLiveTextFormat(liveRoomCreateBody);

        if (!queryService.isExistUserId( liveRoomCreateBody.getUserId() )) {
            throw new NoSuchUserException("没有这个主播", UserErrorRc.NoSuchUserError);
        }
        //获取这个主播的网易账号信息，注册网易账号
        NIMUser nimUserInfo = queryService.getNimUserInfo( liveRoomCreateBody.getUserId() );
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
        LiveRoom liveRoom = new LiveRoom(liveRoomCreateBody,liveRoomId,chatroom.getChatroom().getRoomid());
        liveDao.addLiveRoom(liveRoom);
        return liveRoom;
    }

    /**
     * 创建直播
     * */
    public Live createLive(LiveBody liveBody) throws LiveException {

        LiveRoom liveRoom = liveCache.get(liveBody.getLiveRoomId());
        if(liveRoom == null){
            throw new LiveException("直播间不存在",LiveErrorRc.NoSuchLiveRoom);
        }

        //配置直播转码模板
        aliLiveService.addLiveStreamTranscode(liveBody.getAppName(), "lld");
        aliLiveService.addLiveStreamTranscode(liveBody.getAppName(), "lsd");
        aliLiveService.addLiveStreamTranscode(liveBody.getAppName(), "lhd");

        //开始视频录制
        aliLiveService.startRecordByAppName( liveBody.getAppName() );
        //直播id
        String liveId = liveCounter.getId();
        Live live = new Live(liveId, liveBody);

        liveDao.addLive(live);
        return live;
    }

    /**
     * 开始直播（视频推流）
     */
    public void startLive(String liveRoomId) throws  LiveException{

        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getCurrentLive();
        if(liveRoom.getStatus() != LiveRoomStatus.CLOSE){
            throw new LiveException("当前直播间未开启,不能进行直播！",LiveErrorRc.LiveStatusError);
        }
        if(live == null){
            throw new LiveException("当前直播间不存在直播",LiveErrorRc.NoSuchLiveError);
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
     * @param liveId
     * @throws Exception
     */
    public void stopLive(String liveId)  throws Exception{

        LiveRoom liveRoom = liveCache.get( liveId );
        if( liveRoom == null )
            throw new Exception( "该直播不存在" );
        if( liveRoom.getStatus() != LiveRoomStatus.OPEN )
            throw new Exception( "直播停止失败，当前直播状态：" + liveRoom.getStatus() );
        //停止流的推送
        aliLiveService.finishLiveRequest(liveRoom.getLive(liveRoom.getCurrentLiveId()).getAppName(), liveRoom.getLive(liveRoom.getCurrentLiveId()).getLiveStream());
        //修改直播状态
        liveDao.changeLiveStatus(liveId, LiveRoomStatus.CLOSE);
    }

    /**
     * 修改直播信息
     * @param liveData
     * @throws Exception
     */
    public void changeLive(LiveChangeBody liveData) throws Exception {

        if (liveData == null || liveData.getLiveId() == null) {
            throw new Exception("请填写直播id");
        }
        //验证该直播是否存在
        liveCache.get(liveData.getLiveId());
      //  confirmLiveTextError(liveData.getStream(), liveData.getAppName(), liveData.getTitle(), liveData.getUserIntro(), liveData.getLiveNotice(), liveData.getStartTime());

        liveDao.changeLive(liveData);
    }

    /**
     * 修改直播状态
     * @param liveId
     * @param status
     * @throws Exception
     */
    public void changeLiveStatus(String liveId,LiveRoomStatus status) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if( !status.equals( liveRoom.getStatus() ) ){


            if(status.equals(LiveRoomStatus.OPEN) && !liveRoom.getStatus().equals(LiveRoomStatus.OPEN)){
                aliLiveService.startLiveRequest(liveRoom.getLive(liveId).getAppName(), liveRoom.getLive(liveRoom.getCurrentLiveId()).getLiveStream());
                liveDao.changeLiveStatus(liveId, status);
            }else if(!status.equals(LiveRoomStatus.OPEN)){
                aliLiveService.finishLiveRequest(liveRoom.getLive(liveId).getAppName(), liveRoom.getLive(liveRoom.getCurrentLiveId()).getLiveStream());
                liveDao.changeLiveStatus(liveId, status);
            }
        }
    }

    /**
     * 转码配置
     * @param definition
     * @throws Exception
     */
    public void transCodeConfig(String definition, String liveRoomId) throws Exception {
        if (definition.charAt(0) == 'm' && definition.length() != 3)
            throw new Exception("混流转码只能配置一种模板");
        LiveRoom liveRoom = liveCache.get(liveRoomId);
        Live live = liveRoom.getLive(liveRoom.getCurrentLiveId());
        if (liveRoom.getStatus().equals(LiveRoomStatus.OPEN))
            throw new Exception("直播过程中无法更改转码配置");
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
            if (nowDefinitions != null && deleteDefinition.charAt(0) == 'm') {
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
     * 开始录制
     * @param liveId
     * @throws Exception
     */
    public void startRecord(String liveId) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        Live live = liveRoom.getLive(liveRoom.getCurrentLiveId());
        if (liveRoom.isRecord()) {
            throw new Exception("录制开关已经打开");
        }
        //假如改直播间正处于直播状态，则自动切断再重新开始直播
        if (liveRoom.getStatus().equals(LiveRoomStatus.OPEN)) {
            aliLiveService.finishLiveRequest(live.getAppName(), live.getLiveStream());
            aliLiveService.startLiveRequest(live.getAppName(), live.getLiveStream());
        }
        aliLiveService.startRecordByAppName(live.getAppName());
        liveDao.changeRecordStatus(liveId, 1);
    }

    /**
     * 停止视频录制
     * @param liveId
     * @throws Exception
     */
    public void stopRecord(String liveId) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        Live live = liveRoom.getLive(liveRoom.getCurrentLiveId());
        if (!liveRoom.isRecord()) {
            throw new Exception("录制开关已经关闭");
        }
        aliLiveService.stopLiveRecordByAppName(live.getAppName());
    }

    /*public String addLiveChapter(LiveBody liveBody) throws LiveException {
        LiveRoom liveRoom = liveCache.get(liveBody.getLiveRoomId());
        if (liveRoom.getStatus().equals(LiveRoomStatus.FINISH)) {
            throw new LiveException("直播已结束无法添加章节", LiveErrorRc.LiveStatusError);
        }
        confirmLiveChapter(liveChapter, liveRoom);
        String chapterId = chapterCounter.getId();
        liveDao.addNewChapter(liveChapter, chapterId);
        return chapterId;
    }

    public void changeLiveChapter(LiveChapterChangeBody changeBody) throws LiveException {
        if (changeBody == null) {
            throw new LiveException("修改的内容不能为空", LiveErrorRc.NullError);
        }
        LiveBody liveChapter = changeBody.getLiveBody();
        LiveRoom liveRoom = liveCache.get(liveChapter.getLiveId());
        //检测是该章节是否有该章节
        liveRoom.getLiveChapter(changeBody.getChapterId());
        confirmLiveChapter(liveChapter, liveRoom);
        liveDao.changeChapter(liveChapter, changeBody.getChapterId());
    }

    public void deleteChapter(String chapterId, String liveId) throws LiveException {
        LiveRoom liveRoom = liveCache.get(liveId);
        if (liveRoom.getStatus().equals(LiveRoomStatus.LIVING) && chapterId.equals(liveRoom.getCurrentChapterId())) {
            throw new LiveException("Current chapter is Living,can't delete living chapter", LiveErrorRc.LiveStatusError);
        }
        String newChapterId = liveRoom.getNewChapterId(chapterId);
        liveDao.deleteChapter(liveId, chapterId, newChapterId, chapterId.equals(liveRoom.getCurrentChapterId()));
    }

    public void changeChapterVideo(String liveId, String chapterId, String videoId) throws LiveException {
        LiveRoom liveRoom = liveCache.get(liveId);
        liveRoom.getLiveChapter(chapterId);
        if (!queryService.isExistVideo(videoId)) {
            throw new LiveException("视频不存在,id:" + videoId, LiveErrorRc.NoSuchChapterError);
        }
        liveDao.changeChapterVideo(liveId, chapterId, videoId);
    }



    public String getLiveAuthenticationKey(String app,String stream){
        return aliLiveService.getLivePushAuthenticationKey(app,stream);
    }

    private void confirmLiveTextFormat(LiveRoomCreateBody body) throws LiveException {
        confirmLiveTextFormat(body.getStreamName(), body.getAppName(), body.getLiveTitle(), body.getUserIntro(), body.getLiveNotice(), body.getStartTime());
    }

    private void confirmLiveTextFormat(String stream, String app, String title, String userIntro,
                                      String liveNotice, Date startTime) throws LiveException {
        if (isTextOver(stream, 50) || !isNumberOrChar(stream)) {
            throw new LiveTextFormatException("流名称格式错误",LiveErrorRc.TextFormatError);
        }
        if (isTextOver(app, 50) || !isNumberOrChar(app)) {
            throw new LiveTextFormatException("应用名称格式错误",LiveErrorRc.TextFormatError);
        }
        if (isTextOver(title, 50)) {
            throw new LiveTextFormatException("直播名称格式错误",LiveErrorRc.TextFormatError);
        }
        if (isTextOver(userIntro, 1500)) {
            throw new LiveTextFormatException("专家介绍字数超过限制",LiveErrorRc.TextFormatError);
        }
        if (isTextOver(liveNotice, 150)) {
            throw new LiveTextFormatException("直播公告字数超过限制",LiveErrorRc.TextFormatError);
        }
        if ( ( new Date().getTime() - startTime.getTime() ) > 0 ) {
            throw new LiveTextFormatException("直播时间错误",LiveErrorRc.NoSuchChapterError);
        }
    }

    private void confirmLiveChapter(LiveBody liveChapter, LiveRoom liveRoom) throws LiveException {

        if (isTextOver(liveChapter.getTitle(), 50)) {
            throw new LiveException("章节标题格式错误", LiveErrorRc.TextFormatError);
        }
        if (isTextOver(liveChapter.getContent(), 2500)) {
            throw new LiveException("章节内容格式错误", LiveErrorRc.TextFormatError);
        }
        if (liveChapter.getStartTime().getTime() > liveChapter.getFinishTime().getTime()) {
            throw new LiveException("章节开始时间不能在结束之前之后", LiveErrorRc.TextFormatError);
        }
        if (liveChapter.getStartTime().getTime() < System.currentTimeMillis()) {
            throw new LiveException("章节开始时间不能在当前时间之前", LiveErrorRc.TextFormatError);
        }
    }

    public void recordCallback(RecordCallbackBody body) {
        //空则是录制文件生成事件否则是录制状态改变事件
        try {
            LOGGER.info("ali record request" + body.getEvent());
            String liveId = queryService.getLiveIdByApp(body.getApp());
            if (body.getEvent() == null) {
                liveDao.addRecordVideo(liveId, new Date(body.getStartTime() * 1000), new Date(body.getStopTime() * 1000), body.getDuration(), "http://qzlivevideo.oss-cn-shanghai.aliyuncs.com/" + body.getUri());
            } else {
                liveCache.get(liveId);
                int status = 0;
                if (body.getEvent().equals("record_resumed") || body.getEvent().equals("record_started")) {
                    status = 1;
                }
                liveDao.changeRecordStatus(liveId,status);
            }
        } catch (Exception e) {
            LOGGER.error("ali record callback error：", e);
        }
    }*/

    private boolean isNumberOrChar(String str) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    private boolean isTextOver(String text, int length) {

        return text.length() > length;
    }
}

