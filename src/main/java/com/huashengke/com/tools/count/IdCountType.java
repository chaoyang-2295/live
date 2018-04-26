package com.huashengke.com.tools.count;

/**
 * 根据传入的计数缓存类型，
 */
public enum  IdCountType {

    live(100000,"live"),
    caster(100000,"caster"),
    stream(100000,"stream"),
    liveRoom(100000,"liveRoom");

    private int defaultNumber;
    private String joinString;

    IdCountType(int defaultNumber, String joinString) {
        this.defaultNumber = defaultNumber;
        this.joinString = joinString;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }
    public String getJoinString(){
        return  joinString;
    }
}
