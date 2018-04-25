package com.huashengke.com.live.body;

import java.util.List;

/**
 * Created by yangc on 2018/4/23.
 */
public class VideoLayer {

    /**
     *视频元素高度归一化值，设置高度后宽度会按照等比例缩放,宽度与高度比例值同时设置时后设置的比例生效
     */
    private Float heightNormalized;

    /**
     *视频元素宽度归一化值，设置高度后宽度会按照等比例缩放,宽度与高度比例值同时设置时后设置的比例生效
     */
    private Float widthNormalized;

    /**
     *视频元素参考坐标系,默认topLeft
     * {
         topLeft
         topRight
         bottomLeft
         bottomRight
         center
         topCenter
         bottomCenter
         leftCenter
         rightCenter
     }
     */
    private String positionRefer;

    /**
     * 视频元素归一化坐标(x,y)值
     */
    private List<Float> positionNormalizeds;

    /**
     * 视频坐标ID
     */
    private String locationId;


    public Float getHeightNormalized() {
        return heightNormalized;
    }

    public void setHeightNormalized(Float heightNormalized) {
        this.heightNormalized = heightNormalized;
    }

    public Float getWidthNormalized() {
        return widthNormalized;
    }

    public void setWidthNormalized(Float widthNormalized) {
        this.widthNormalized = widthNormalized;
    }

    public String getPositionRefer() {
        return positionRefer;
    }

    public void setPositionRefer(String positionRefer) {
        this.positionRefer = positionRefer;
    }

    public List<Float> getPositionNormalizeds() {
        return positionNormalizeds;
    }

    public void setPositionNormalizeds(List<Float> positionNormalizeds) {
        this.positionNormalizeds = positionNormalizeds;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
