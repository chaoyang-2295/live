package com.huashengke.com.live.body;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class LiveRoomCreateBody {

    @ApiModelProperty("主播ID")
    private String userId;

    @ApiModelProperty("主播介绍")
    private String userIntro;

    @ApiModelProperty("直播标题")
    private String liveTitle;

    @ApiModelProperty("直播公告")
    private String liveNotice;

    @ApiModelProperty("直播码率")
    private String definition;


    public static void main(String [] args){

        Gson gson = new Gson();

        LiveRoomCreateBody body = new LiveRoomCreateBody("123456","title","userIntro","liveNotice","definition");

        System.out.println(gson.toJson(body));

    }

    public LiveRoomCreateBody() {
    }

    public LiveRoomCreateBody(String userId, String title, String userIntro, String liveNotice, String definition) {
        this.userId = userId;
        this.userIntro = userIntro;
        this.liveTitle = title;
        this.liveNotice = liveNotice;
        this.definition = definition;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public String getUserId() {
        return userId;
    }

    public String getLiveNotice() {
        return liveNotice;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public String getDefinition() {
        return definition;
    }
}
