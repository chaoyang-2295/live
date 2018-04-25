package com.huashengke.com.live.dao;

import com.huashengke.com.live.body.*;
import com.huashengke.com.live.mapper.LiveMapper;
import com.huashengke.com.tools.exception.live.LiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class NewLiveDao{

    @Autowired
    private LiveMapper liveMapper;

    @Autowired
    protected NewLiveDao() {}
    public LiveRoom getLive(String liveRoomId){

        return  liveMapper.getLive( liveRoomId );
    }

    
    public void addNewStream( String liveId, String stream, String description){
        liveMapper.addNewStream(stream,description,liveId);
    }

    public void changeStreamDescription( String liveId, String stream, String description) throws LiveException {
        liveMapper.changeStreamDescription(stream,description,liveId);
    }

    public void changeStreamForbid( String liveId, String stream, int isForbid) throws LiveException {
        liveMapper.changeStreamForbid(stream,liveId,isForbid);
    }

    public void changeStreamShow( String liveId, String stream, int isShow) throws LiveException {
        liveMapper.changeStreamShow(stream,liveId,isShow);
    }

    public void changeStreamPush( String liveId, String stream, int isPush) throws LiveException {
        liveMapper.changeStreamPush(stream,liveId,isPush);
    }

    public void changeMainStreamPush( String liveId, String stream, int isPush) throws LiveException {
        liveMapper.changeStreamPush(stream,liveId,isPush);
        liveMapper.changeLivePush(liveId,isPush);
    }

    public void changeMainStream( String liveId, String newStream, String oldStream) throws LiveException {
        liveMapper.changeMainStream(newStream,liveId,1);
        liveMapper.changeMainStream(oldStream,liveId,0);
        liveMapper.changeLiveStream(newStream,liveId);
    }

    public void changeLiveStreamStatus(String liveId, LiveStreamStatus streamName){

    }

    public void addLiveRoom(LiveRoom liveRoom) throws LiveException {

        //插入直播房间信息
        liveMapper.addLiveRoom(liveRoom.getId(), liveRoom.getTitle(), liveRoom.getCover(), liveRoom.getStatus().name(),
                liveRoom.getChatRoomId(), liveRoom.getUserId(), liveRoom.getUserIntro(), liveRoom.getLiveNotice(),
                liveRoom.getDefinition());
    }

    public void addLive(Live live){

//        liveMapper.addLiveRecord();

    }

    public void changeLive( LiveChangeBody live) throws LiveException {
        liveMapper.changeLive(live);
    }

    public void changeLiveRoom(LiveRoomChangeBody body){


    }

    public void changeDefinition( String liveId, String definition) throws LiveException {
        liveMapper.changeDefinition(liveId,definition);
    }

    public void addRecordVideo( String liveId, Date startTime, Date endTime, long duration, String uri) throws LiveException {
        liveMapper.addLiveRecord(liveId, startTime, endTime, duration, uri);
    }

    public void changeLiveRoomStatus(LiveRoom liveRoom){
        liveMapper.changeLiveRoomStatus(liveRoom, LiveRoomStatus.OPEN.name());
    }

    public void changeLiveStatus(Live live, LiveStatus liveStatus) throws LiveException {
        liveMapper.changeLiveStatus(live.getId(), liveStatus.name());
        //修改直播流状态
    }

    
    public void changeRecordStatus( String liveId, int recordStatus) throws LiveException {
        liveMapper.changeRecordStatus(liveId, recordStatus);
    }

    public void changeAliRecordStatus( String liveId, int isRecord) throws LiveException {
        liveMapper.changeAliRecordStatus(liveId, isRecord);
    }

    @Transactional
    public void addNewChapter(LiveBody chapter, String chapterId) {
        Live newChapter = new Live(chapterId,chapter);
        liveMapper.addLiveChapter(newChapter);
    }

    @Transactional
    public void changeChapter(LiveBody chapter, String chapterId) {
        liveMapper.changeLiveChapter(new Live(chapterId,chapter));
    }

    @Transactional
    public void deleteChapter(String liveId,String chapterId,String newChapterId,boolean isUpdateChapter) {
        liveMapper.deleteLiveChapter(chapterId);
        if(isUpdateChapter){
            liveMapper.changeNextChapterId(liveId,newChapterId);
        }
    }

    public void changeChapterVideo(String liveId, String chapterId, String videoId) {
        liveMapper.changeLiveChapterVideo(videoId,chapterId);
    }

    public void changeMaxOnlineNumber(String liveId, String chapterId, int maxOnlineNumber) {
        liveMapper.changeMaxOnlineNumber(chapterId, maxOnlineNumber);
    }


}
