package com.huashengke.com.live.body;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LiveRoomChangeBody {

    @ApiModelProperty("直播间ID")
    private String liveRoomId;

    @ApiModelProperty("主播介绍")
    private String userIntro;

    @ApiModelProperty("直播标题")
    private String liveTitle;

    @ApiModelProperty("直播公告")
    private String liveNotice;

    @ApiModelProperty("web封面")
    private String cover;

    public LiveRoomChangeBody(){}

    public String getLiveRoomId() {
        return liveRoomId;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public String getLiveNotice() {
        return liveNotice;
    }

    public String getCover() {
        return cover;
    }
}
