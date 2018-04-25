package com.huashengke.com.live.body;

import java.util.List;

public class LiveStatusData {
    /**直播状态*/
    private LiveRoomStatus liveRoomStatus;
    /**在线人数*/
    private long onlineNumber;
    /**转码信息*/
    private List<DefinitionShow> definitions;
    /**直播链接*/
    private String url;
    /**直播是否推流*/
    private boolean isPush;

    private String authenticationKey;

    public LiveStatusData(String url, LiveRoom liveRoom, int onlineNumber, String authenticationKey) {

        this.url = url;
        this.onlineNumber = onlineNumber;
        this.liveRoomStatus = liveRoom.getStatus();
        this.authenticationKey = authenticationKey;
    }

    public LiveRoomStatus getLiveRoomStatus() {
        return liveRoomStatus;
    }

    public long getOnlineNumber() {
        return onlineNumber;
    }

    public List<DefinitionShow> getDefinitions() {
        return definitions;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public boolean isPush() {
        return isPush;
    }
}
