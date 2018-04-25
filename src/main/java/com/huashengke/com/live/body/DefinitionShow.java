package com.huashengke.com.live.body;

public class DefinitionShow {
    /**超清/高清/流畅*/
    private String name;
    /** lld_lsd_lhd */
    private String type;

    public DefinitionShow(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
