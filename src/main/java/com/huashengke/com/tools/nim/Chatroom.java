package com.huashengke.com.tools.nim;


public class Chatroom {
    /**聊天室房间号*/
    private String roomid;

    private boolean valid;
    /**公告*/
    private String announcement;

    private boolean muted;
    /**聊天室标题*/
    private String name;
    /**直播地址*/
    private String broadcasturl;
    /**其他信息*/
    private String ext;
    /**聊天室创建者*/
    private String creator;

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBroadcasturl() {
        return broadcasturl;
    }

    public void setBroadcasturl(String broadcasturl) {
        this.broadcasturl = broadcasturl;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
