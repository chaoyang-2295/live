package com.huashengke.com.live.body;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Created by yangc on 2018/4/13.
 */
@ApiModel
public class CasterCreateBody {

    @ApiModelProperty("主播ID")
    private String userId;

    @ApiModelProperty("导播台名称")
    private String casterName;

    @ApiModelProperty("用于保证请求的幂等性。由客户端生成该参数值，要保证在不同请求间唯一，最大不值过 64 个 ASCII 字符")
    private String clientToken;

    @ApiModelProperty("延时时间")
    private float delay;

    @ApiModelProperty("备播视频，媒体资源库ID")
    private String UrgentMaterialId;


    public Float getDelay() {
        return delay;
    }

    public String getUserId() {
        return userId;
    }

    public String getCasterName() {
        return casterName;
    }


    public String getUrgentMaterialId() {
        return UrgentMaterialId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCasterName(String casterName) {
        this.casterName = casterName;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setUrgentMaterialId(String urgentMaterialId) {
        UrgentMaterialId = urgentMaterialId;
    }

    public static void main(String[] args){

        CasterCreateBody body = new CasterCreateBody();

        body.setCasterName("导播台");
        body.setDelay(10);
        body.setUserId("123456");
        body.setClientToken(UUID.randomUUID().toString());

        Gson gson = new Gson();
        System.out.println(gson.toJson(body));

    }
}
