package com.huashengke.com.tools.nim;

/**
 * Created by chentz on 2017/8/25.
 */
public class NIMChatRoomData {
    private int roomid;
    private boolean valid;
    private boolean muted;
    private String announcement;
    private String name;
    private String broadcasturl;
    private int onlineusercount;
    private String ext;
    private String creator;

    public int getRoomid() {
        return roomid;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isMuted() {
        return muted;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public String getName() {
        return name;
    }

    public String getBroadcasturl() {
        return broadcasturl;
    }

    public int getOnlineusercount() {
        return onlineusercount;
    }

    public String getExt() {
        return ext;
    }

    public String getCreator() {
        return creator;
    }
}
