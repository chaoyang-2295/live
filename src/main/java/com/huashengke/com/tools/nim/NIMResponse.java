package com.huashengke.com.tools.nim;

public class NIMResponse {
    private int code;
    private String desc;

    public NIMResponse() {

    }

    public NIMResponse(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
