package com.huashengke.com.tools.nim;

import java.util.List;

/**
 * Created by chentz on 2018/1/15.
 */
public class TeamInfo {
    private String tname;
    private String announcement;
    private String owner;
    private int maxusers;
    private String joinmode;
    private int tid;
    private String intro;
    private int size;
    private String custom;
    private boolean mute;
    private List<String> admins;
    private List<String> members;

    public String getTname() {
        return tname;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public String getOwner() {
        return owner;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public String getJoinmode() {
        return joinmode;
    }

    public int getTid() {
        return tid;
    }

    public String getIntro() {
        return intro;
    }

    public int getSize() {
        return size;
    }

    public String getCustom() {
        return custom;
    }

    public boolean isMute() {
        return mute;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public List<String> getMembers() {
        return members;
    }
}
