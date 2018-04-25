package com.huashengke.com.live.body;

public enum  MediaType {
    RTMP(""),
    M3U8(".m3u8"),
    FLV(".flv")
    ;
    private String mediaStr;

    MediaType(String mediaStr) {
        this.mediaStr = mediaStr;
    }

    public String getMediaStr() {
        return mediaStr;
    }
}
