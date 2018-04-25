package com.huashengke.com.live.body;

/**
 * Created by yangc on 2018/4/23.
 */
public class AudioLayer {

    private String locationId;

    private Float volumeRate;

    private String validChannel;



    public Float getVolumeRate() {
        return volumeRate;
    }

    public void setVolumeRate(Float volumeRate) {
        this.volumeRate = volumeRate;
    }

    public String getValidChannel() {
        return validChannel;
    }

    public void setValidChannel(String validChannel) {
        this.validChannel = validChannel;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
