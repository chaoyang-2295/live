package com.huashengke.com.live.body;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yangc on 2018/4/25.
 */
@ApiModel("导播台视频源body")
public class CasterVideoBody {

    @ApiModelProperty("导播台ID")
    private String casterId;

    @ApiModelProperty("指定视频源的位置")
    private String locationId;

    @ApiModelProperty("视频源名称")
    private String resourceName;

    @ApiModelProperty("直播流地址,视频源类型为直播流时必填")
    private String liveStreamUrl;

    @ApiModelProperty("媒资库素材Id,视频源类型为视频点播素材时必填")
    private String materialId;

    @ApiModelProperty("仅对文件视频有效，表示播放完后重复继续播放的次数。默认值：0，表示不重复播放。-1：表示一直循环重复。")
    private int repeatNum;

    public String getCasterId() {
        return casterId;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getLiveStreamUrl() {
        return liveStreamUrl;
    }

    public String getMaterialId() {
        return materialId;
    }

    public int getRepeatNum() {
        return repeatNum;
    }
}
