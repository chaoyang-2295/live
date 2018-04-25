package com.huashengke.com.live.body;

/**
 * 播放画质
 */
public enum Definition {
    hd("高清"),
    ld("流畅"),
    ud("超清"),
    sd("标清"),
    lhd("高清"),
    lld("流畅"),
    lud("超清"),
    lsd("标清"),
    od("原画"),
    msd("标清"),
    mhd("高清");

    private String name;

    Definition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
