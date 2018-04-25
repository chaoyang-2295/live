package com.huashengke.com.live.body;

import java.util.ArrayList;
import java.util.Date;


public class Live {

    /**
     *直播ID
     */
    private String id;
    /**
     *直播标题
     */
    private String title;
    /**
     *直播内容
     */
    private String content;
    /**
     *AppName
     */
    private String appName;
    /**
     *StreamName
     */
    private String streamName;
    /**
     *直播间ID
     */
    private String liveRoomId;
    /**
     *直播状态
     */
    private LiveStatus status;
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
        this.title = liveBody.getTitle();
        this.status = LiveStatus.NOT_START;
        this.content = liveBody.getContent();
        this.appName = liveBody.getAppName();
        this.streamName = liveBody.getStreamName();
        this.liveRoomId = liveBody.getLiveRoomId();
        this.liveStreams = new ArrayList<>();
        this.maxOnlineNumber = 0;
    }

    public String getId() {
        return id;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    public LiveStatus getStatus() {
        return status;
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
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", appName='" + appName + '\'' +
                ", streamName='" + streamName + '\'' +
                ", liveRoomId='" + liveRoomId + '\'' +
                ", status=" + status +
                ", maxOnlineNumber=" + maxOnlineNumber +
                ", isRecord=" + isRecord +
                ", liveStreams=" + liveStreams +
                '}';
    }
}
