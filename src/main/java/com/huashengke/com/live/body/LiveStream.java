package com.huashengke.com.live.body;

/**
 * 直播流
 */
public class LiveStream {
    /**流名称*/
    private String stream;
    /**直播id*/
    private String liveId;
    /**描述*/
    private String description;
    /**是否被禁止*/
    private boolean isForbid;
    /**是否显示*/
    private boolean isShow;
    /**是否拉流*/
    private boolean isPush;
    /**是否是主流*/
    private boolean isMain;

    public LiveStream() {
    }

    public LiveStream(String stream, String description, String liveId) {
        this.stream = stream;
        this.description = description;
        this.liveId = liveId;
        this.isForbid = true;
        this.isShow = false;
        this.isPush = false;
        this.isMain = false;
    }

    public LiveStream(String stream, String liveId) {
        this.stream = stream;
        this.description = "默认主麦";
        this.liveId = liveId;
        this.isForbid = false;
        this.isShow = true;
        this.isPush = false;
        this.isMain = true;
    }

    public LiveStream(String stream, String description, String liveId, boolean isForbid, boolean isShow, boolean isPush, boolean isMain) {
        this.stream = stream;
        this.description = description;
        this.isForbid = isForbid;
        this.isShow = isShow;
        this.isPush = isPush;
        this.isMain = isMain;
        this.liveId = liveId;
    }

    public String getStream() {
        return stream;
    }

    public String getDescription() {
        return description;
    }

    public boolean isForbid() {
        return isForbid;
    }

    public boolean isShow() {
        return isShow;
    }

    public boolean isPush() {
        return isPush;
    }

    public boolean isMain() {
        return isMain;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setForbid(boolean forbid) {
        isForbid = forbid;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void setPush(boolean push) {
        isPush = push;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }
}
