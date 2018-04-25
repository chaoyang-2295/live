package com.huashengke.com.tools.nim;

/**
 * Created by xujunbo on 17-6-23.
 */
public class RegisterInfo {
    private String accid;
    private String name;
    private String token;

    public RegisterInfo() {
    }

    public RegisterInfo(String accid, String name, String token) {
        this.accid = accid;
        this.name = name;
        this.token = token;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
