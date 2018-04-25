package com.huashengke.com.tools.nim;

/**
 * Created by chentz on 2017/12/12.
 */
public enum TeamChatType {
    PayAnswer("pa"),
    Requirement("r");
    private String shortName;

    TeamChatType(String shortName) {
        this.shortName = shortName;
    }

    public String getAccId(int autoId) {
        return shortName+autoId;
    }
}
