package com.huashengke.com.live.body;


public class LiveAuthenticationData {

    private String  authenticationKey;

    private long authenticationTimestamp;

    private String url;

    public LiveAuthenticationData(String authenticationKey, long authenticationTimestamp, String url) {
        this.authenticationKey = authenticationKey;
        this.authenticationTimestamp = authenticationTimestamp;
        this.url = url;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public long getAuthenticationTimestamp() {
        return authenticationTimestamp;
    }

    public String getUrl() {
        return url;
    }
}
