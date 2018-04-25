package com.huashengke.com.live.body;

/**
 * Created by yangc on 2018/4/25.
 */
public enum LiveStreamStatus {

    /**
     * 未进行推送
     */
    Stop(0),

    /**
     * 正在进行推送,推送不一定显示
     */
    Push(1);


    private int val;
    LiveStreamStatus(int val){
        this.val = val;
    }

}
