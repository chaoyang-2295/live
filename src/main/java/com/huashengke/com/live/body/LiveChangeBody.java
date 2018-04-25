package com.huashengke.com.live.body;

import java.util.Date;

public class LiveChangeBody{
    /**
     * 直播id
     */
    private String liveId;
    /**
     * 主播简介
     */
    private String userIntro;

    /**
     * 直播标题
     */
    private String title;

    /**
     * 直播公告
     */
    private String liveNotice;

    /**
     * 直播时间介绍
     */
    private Date startTime;

    /**
     * web封面
     */
    private String webCover;


    public LiveChangeBody() {
    }

    public LiveChangeBody(String title, String liveNotice, Date startTime,String userIntro,String webCover, String liveId) {
        this.title = title;
        this.liveNotice = liveNotice;
        this.startTime = startTime;
        this.userIntro = userIntro;
        this.webCover = webCover;
        this.liveId = liveId;
    }

    public String getTitle() {
        return title;
    }

    public String getLiveNotice() {
        return liveNotice;
    }

    public String getWebCover() {
        return webCover;
    }

    public String getLiveId() {
        return liveId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getUserIntro() {
        return userIntro;
    }
}
