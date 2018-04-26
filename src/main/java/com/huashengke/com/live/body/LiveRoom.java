package com.huashengke.com.live.body;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chentz on 2017/11/8.
 */
public class LiveRoom {
    /**
     * 直播间ID
     */
    private String id;
    /**
     * 直播间标题
     */
    private String title;
    /**
     * 主播ID
     */
    private String userId;
    /**
     * 主播介绍
     */
    private String userIntro;
    /**
     * 直播公告
     */
    private String liveNotice;
    /**
     * 直播间状态  OPEN  CLOSE
     */
    private LiveRoomStatus status;

    /**
     * 当前进行的直播
     */
    private Live currentLive;

    /**
     * 当前直播ID
     */
    private String currentLiveId;

    /**
     * 聊天室id
     */
    private String chatRoomId;
    /**
     * 转码文字描述
     */
    private String definition;

    /**
     * 直播转码
     */
    private List<DefinitionShow> definitions;

    public LiveRoom() {
    }

    public LiveRoom(LiveRoomCreateBody liveRoomCreateBody, String liveRoomId, String chatRoomId) {
        this.changeBaseInfo(liveRoomCreateBody);
        this.id = liveRoomId;
        this.chatRoomId = chatRoomId;
        this.status = LiveRoomStatus.OPEN;
        this.definition = "lld_lsd_lhd";
        this.definitions = getDefinitionsByDefinition("lld_lsd_lhd");
    }


    public void changeBaseInfo(LiveRoomCreateBody liveRoomCreateBody) {
        this.title = liveRoomCreateBody.getLiveTitle();
        this.userId = liveRoomCreateBody.getUserId();
        this.userIntro = liveRoomCreateBody.getUserIntro();
        this.liveNotice = liveRoomCreateBody.getLiveNotice();
    }

    private List<DefinitionShow> getDefinitionsByDefinition(String definition) {
        if (definition != null && !definition.equals("")) {
            String[] definitionStrs = definition.split("_");
            return Arrays.stream(definitionStrs).
                    map(s -> new DefinitionShow(Definition.valueOf(s).getName(), s)).
                    collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void init(Live live){
        this.currentLive = live;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public List<DefinitionShow> getDefinitions() {
        return definitions;
    }

    public int getMaxOnlineNumber() {
      return 0;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public String getLiveNotice() {
        return liveNotice;
    }

    public LiveRoomStatus getStatus() {
        return status;
    }

    public String getDefinition() {
        return definition;
    }

    public Live getCurrentLive() {
        return currentLive;
    }

    public String getCurrentLiveId() {
        return currentLiveId;
    }


    @Override
    public String toString() {
        return "LiveRoom{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", userId='" + userId + '\'' +
                ", userIntro='" + userIntro + '\'' +
                ", liveNotice='" + liveNotice + '\'' +
                ", status=" + status +
                ", currentLive=" + currentLive +
                ", currentLiveId='" + currentLiveId + '\'' +
                ", chatRoomId='" + chatRoomId + '\'' +
                ", definition='" + definition + '\'' +
                ", definitions=" + definitions +
                '}';
    }
}
