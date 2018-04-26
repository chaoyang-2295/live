package com.huashengke.com.live.body;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class Live {

    /**
     *直播ID
     */
    private String id;
    /**
     *AppName
     */
    private String appName;
    /**
     *主流
     */
    private String streamName;
    /**
     *直播间ID
     */
    private String liveRoomId;
    /**
     *直播状态
     */
    private LiveStatus liveStatus;
    /**
     *在线人数
     */
    private int maxOnlineNumber;

    /**
     * 是否开始录制
     */
    private boolean isRecord;

    /**
     * 直播中的流
     */
    private ArrayList<LiveStream> liveStreams;
    public Live() {
    }

    public Live(String id, LiveBody liveBody) {
        this.id = id;
        this.isRecord = false;
        this.liveStatus = LiveStatus.NOT_START;
        this.appName = liveBody.getAppName();
        this.streamName = liveBody.getStreamName();
        this.liveRoomId = liveBody.getLiveRoomId();
        this.liveStreams = new ArrayList<>();
        this.maxOnlineNumber = 0;
    }

    public void addLiveStream(LiveStream stream){
        if(liveStreams.contains(stream)){
            return;
        }
        liveStreams.add(stream);
    }
    public String getId() {
        return id;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public String getAppName() {
        return appName;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getLiveRoomId() {
        return liveRoomId;
    }

    public LiveStatus getLiveStatus() {
        return liveStatus;
    }

    public int getMaxOnlineNumber() {
        return maxOnlineNumber;
    }

    public ArrayList<LiveStream> getLiveStreams() {
        return liveStreams;
    }





    @Override
    public String toString() {
        return "Live{" +
                "id='" + id + '\'' +
                ", appName='" + appName + '\'' +
                ", streamName='" + streamName + '\'' +
                ", liveRoomId='" + liveRoomId + '\'' +
                ", liveStatus=" + liveStatus +
                ", maxOnlineNumber=" + maxOnlineNumber +
                ", isRecord=" + isRecord +
                ", liveStreams=" + liveStreams +
                '}';
    }
}
