package com.huashengke.com.live.body;


public class LiveChangeBody{
    /**
     * 直播id
     */
    private String liveId;

    /**
     * 主播簡介
     */
    private String userIntro;

    /**
     * 直播标题
     */
    private String title;

    /**
     * 直播内容
     */
    private String content;

    /**
     * 直播公告
     */
    private String liveNotice;

    /**
     * web封面
     */
    private String webCover;


    public LiveChangeBody() {
    }

    public LiveChangeBody(String title, String liveNotice, String content,String userIntro,String webCover, String liveId) {
        this.title = title;
        this.liveNotice = liveNotice;
        this.userIntro = userIntro;
        this.content = content;
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

    public String getUserIntro() {
        return userIntro;
    }

    public String getContent() {
        return content;
    }
}
