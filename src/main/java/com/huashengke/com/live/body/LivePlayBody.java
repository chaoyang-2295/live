package com.huashengke.com.live.body;
import java.util.List;

public class LivePlayBody {
    private LiveContentBody liveContent;
    private int onlineNumber;
    /**
     * 直播公告
     */
    private String liveNotice;
    /**聊天室id*/
    private String chatRoomId;
    /**是否开始推流*/
    private boolean isPushStream;
    /**
     * 清晰度 hd高清 ld流畅 ud 超清 sd标清
     */
    private List<DefinitionShow> definitions;
    /**直播地址*/
    private String url;

    private String authenticationKey;

    public LivePlayBody(LiveRoom liveRoom, LiveContentBody liveContent, int onlineNumber, String url, String authenticationKey) {
        this.url = url;
        this.liveContent = liveContent;
        this.liveNotice = liveRoom.getLiveNotice();
        this.chatRoomId = liveRoom.getChatRoomId();
        this.onlineNumber = onlineNumber;
        this.authenticationKey = authenticationKey;
    }

    public LiveContentBody getLiveContent() {
        return liveContent;
    }

    public int getOnlineNumber() {
        return onlineNumber;
    }

    public String getLiveNotice() {
        return liveNotice;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public boolean isPushStream() {
        return isPushStream;
    }

    public List<DefinitionShow> getDefinitions() {
        return definitions;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public String getUrl() {
        return url;
    }
}
