package com.huashengke.com.tools.nim;

/**
 * Created by xujunbo on 17-6-23.
 */
public class NIMRegisterResponse extends NIMResponse {
    private RegisterInfo info;

    public NIMRegisterResponse(RegisterInfo registerInfo) {
        super(200, null);
        setInfo(registerInfo);
    }

    public RegisterInfo getInfo() {
        return info;
    }

    public void setInfo(RegisterInfo info) {
        this.info = info;
    }
}
