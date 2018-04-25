package com.huashengke.com.tools.nim;

/**
 * Created by chentz on 2017/12/12.
 */
public class NIMCreateTeamResponse extends NIMResponse{
    private String tid;

    public  NIMCreateTeamResponse(int code, String desc) {
        super(code, desc);
    }

    public String getTid() {
        return tid;
    }
}
