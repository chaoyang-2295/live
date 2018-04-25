package com.huashengke.com.live.body;

/**
 * Created by chentz on 2017/11/8.
 */
public class LiveChapterChangeBody {
    
    private LiveBody liveBody;
    
    private String chapterId;

    public LiveChapterChangeBody() {
    }

    public LiveChapterChangeBody(LiveBody liveBody, String chapterId) {
        this.liveBody = liveBody;
        this.chapterId = chapterId;
    }

    public LiveBody getLiveBody() {
        return liveBody;
    }

    public String getChapterId() {
        return chapterId;
    }
}
