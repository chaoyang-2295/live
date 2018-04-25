package com.huashengke.com.tools.nim;

/**
 * Created by xujunbo on 17-6-23.
 */
public enum ApplicationType {
    JSON("application/json"),
    XML("application/xml"),
    TEXT("text/xml"),
    FORM("application/x-www-form-urlencoded");

    private String type;

    private ApplicationType(String type) {
        this.type = type;
    }

    public String val() {
        return type;
    }
}
