package com.huashengke.com.live.body;

import java.util.List;

/**
 * Created by yangc on 2018/4/25.
 */
public class CasterLayoutBody {

    private String casterId;

    private List<VideoLayer> videoLayers;

    private List<AudioLayer> audioLayers;


    public String getCasterId() {
        return casterId;
    }

    public void setCasterId(String casterId) {
        this.casterId = casterId;
    }

    public List<VideoLayer> getVideoLayers() {
        return videoLayers;
    }

    public void setVideoLayers(List<VideoLayer> videoLayers) {
        this.videoLayers = videoLayers;
    }

    public List<AudioLayer> getAudioLayers() {
        return audioLayers;
    }

    public void setAudioLayers(List<AudioLayer> audioLayers) {
        this.audioLayers = audioLayers;
    }
}
