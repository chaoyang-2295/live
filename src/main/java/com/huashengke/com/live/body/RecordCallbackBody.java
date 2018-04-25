package com.huashengke.com.live.body;

/**
 * 录制回调
 */
public class RecordCallbackBody {

    /**
     *录制应用名
     */
    private String app;
    /**
     *录制文件存放在OSS Bucket的路径
     */
    private String uri;
    /**
     *加速域名
     */
    private String domain;
    /**
     *流名称
     */
    private String stream;
    /**
     *录制时常
     */
    private long duration;
    /**
     *录制起始时间
     */
    private long startTime;
    /**
     *录制结束时间
     */
    private long stopTime;

    /**
     *录制状态事件,可为---record_started/record_paused/record_resumed
     */
    private String event;

    public String getDomain() {
        return domain;
    }

    public String getApp() {
        return app;
    }

    public String getStream() {
        return stream;
    }

    public String getUri() {
        return uri;
    }

    public long getDuration() {
        return duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public String getEvent() {
        return event;
    }

}
