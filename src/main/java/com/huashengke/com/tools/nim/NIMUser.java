package com.huashengke.com.tools.nim;

public class NIMUser {
    private int autoId;
    private String userId;
    private String username;
    private String avatar;

    private String accid;
    private String token;
    private String nimAppKey;

    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNimAppKey() {
        return nimAppKey;
    }

    public void setNimAppKey(String nimAppKey) {
        this.nimAppKey = nimAppKey;
    }
}
