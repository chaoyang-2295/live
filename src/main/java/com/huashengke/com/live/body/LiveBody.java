package com.huashengke.com.live.body;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;

import java.util.Date;

@ApiModel
public class LiveBody {

    /**
     * 直播标题
     */
    private String title;
    /**
     *直播内容
     */
    private String content;
    /**
     *AppName
     */
    private String appName;
    /**
     * 流名称
     */
    private String streamName;
    /**
     *直播间编号
     */
    private String liveRoomId;

    public static void main(String[] args){

        LiveBody body = new LiveBody( "liveTitle", "content", "appName", "stream01", "liveRoom1000006" );

        Gson gson = new Gson();
        System.out.println( gson.toJson( body ) );
    }



    public LiveBody() {
    }

    public LiveBody(String title, String content, String appName, String streamName, String liveRoomId) {
        this.title = title;
        this.content = content;
        this.appName = appName;
        this.streamName = streamName;
        this.liveRoomId = liveRoomId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAppName() {
        return appName;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getLiveRoomId() {
        return liveRoomId;
    }
}
