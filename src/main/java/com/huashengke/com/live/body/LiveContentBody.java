package com.huashengke.com.live.body;

import java.util.Date;

public class LiveContentBody {
    /**
     * 直播标题
     */
    private String title;
    /**
     * 主播id
     */
    private String  userId;
    /**
     * 专家简介
     */
    private String useIntro;
    /**
     * 直播开始时间
     */
    private Date startTime;
    /**
     * 直播状态
     */
    private LiveRoomStatus liveRoomStatus;

    public LiveContentBody(LiveRoom liveRoom, String userId ) {
        this.userId = userId;
        this.useIntro = liveRoom.getUserIntro();
        this.title = liveRoom.getTitle();
        this.liveRoomStatus = liveRoom.getStatus();
    }


    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getUseIntro() {
        return useIntro;
    }

    public Date getStartTime() {
        return startTime;
    }

    public LiveRoomStatus getLiveRoomStatus() {
        return liveRoomStatus;
    }
}
