package com.huashengke.com.live.service;


import com.aliyuncs.live.model.v20161101.*;
import com.huashengke.com.live.AliClient;
import com.huashengke.com.live.body.AudioLayer;
import com.huashengke.com.live.body.VideoLayer;
import com.huashengke.com.tools.exception.live.LiveException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by yangc on 2018/4/13.
 */
@Service
public class AliCasterRequestService {

    @Autowired
    private AliClient client;


    /**
     * 创建导播台
     *
     */
    public CreateCasterResponse createCaster(String clientToken, String casterName) throws LiveException {

        CreateCasterRequest request = new CreateCasterRequest();
        request.setClientToken(UUID.randomUUID().toString());
        request.setCasterName(casterName);
        request.setNormType(Integer.valueOf(1));
        request.setChargeType("PostPaid");

        return client.getAcsResponse(request);
    }


    /**
     * 导播台配置
     *
     * @param casterId
     * @param delay    延时播放。
     *                 0：禁用延时
     *                 ＞0：启用延时
     *                 单位：秒
     * @throws LiveException
     */
    public void setCasterConfig(String casterId, Float delay, String urgentMaterialId) throws LiveException {

        //配置导播台
        SetCasterConfigRequest request = new SetCasterConfigRequest();
        request.setDelay(delay);
        request.setCasterId(casterId);
        request.setDomainName(client.getDomain());

        //录制配置
        JSONObject recordConfig = new JSONObject();
        recordConfig.put("OssBucket", client.getLiveBucket());
        recordConfig.put("OssEndpoint", client.getEndPoint());
        //配置视频录制模板
        JSONArray videoFormat = new JSONArray();
        videoFormat.add(new JSONObject().put("Format", "mp4"));
        videoFormat.add(new JSONObject().put("OssObjectPrefix", client.getVideoName()));
        recordConfig.put("videoFormat", videoFormat);


        //配置直播转码模板
        JSONArray liveTemplate = new JSONArray();
        liveTemplate.add("lld"); // 直播转码模板,lld-流畅,lsd-标清,lhd-高清,lud-超清
        liveTemplate.add("lhd");
        liveTemplate.add("lud");
        JSONObject transcodeConfig = new JSONObject();
        transcodeConfig.put("LiveTemplate", liveTemplate);
        transcodeConfig.put("CasterTemplate", "lp_ld"); // 导播台分辨率,lp_ld-流畅,lp_sd-标清,lp_hd-高清,lp_ud-超清


        request.setUrgentMaterialId(urgentMaterialId);
        request.setRecordConfig(recordConfig.toString());
        request.setTranscodeConfig(transcodeConfig.toString());

        client.sendRequest(request);
    }


    /**
     * 添加视频源
     *
     * @param casterId      导播台ID
     * @param locationId    用于指定视频源位置，定义布局画面时根据这个参数获取对应视频源进行布局 格式 {RV01 ... RV12}
     * @param videoName     视频源名称
     * @param liveStreamUrl 直播流地址,将直播流作为视频源
     */
    public void addCasterVideo(String casterId, String locationId, String videoName, String liveStreamUrl) throws LiveException {

        AddCasterVideoResourceRequest request = new AddCasterVideoResourceRequest();
        request.setCasterId(casterId);
        request.setResourceName(videoName);
        request.setLocationId(locationId);
        request.setLiveStreamUrl(liveStreamUrl);

        client.sendRequest(request);
    }

    /**
     * 查询视频源列表
     *
     * @param casterId
     * @throws LiveException
     */
    public DescribeCasterVideoResourcesResponse describeCasterVideo(String casterId) throws LiveException {

        DescribeCasterVideoResourcesRequest request = new DescribeCasterVideoResourcesRequest();
        request.setCasterId(casterId);

        return client.getAcsResponse(request);
    }

    /**
     * 删除视频源
     *
     * @param casterId   导播台ID
     * @param resourceId 资源ID
     * @throws LiveException
     */
    public void deleteCasterVideo(String casterId, String resourceId) throws LiveException {

        DeleteCasterVideoResourceRequest request = new DeleteCasterVideoResourceRequest();
        request.setCasterId(casterId);
        request.setResourceId(resourceId);

        client.sendRequest(request);
    }

    /**
     * 添加布局
     *
     * @param casterId
     */
    public void addCasterLayout(String casterId, List<VideoLayer> videoLayers, List<AudioLayer> audioLayers) throws LiveException {

        List<AddCasterLayoutRequest.VideoLayer> aliVideoLayers = new ArrayList<>();
        List<AddCasterLayoutRequest.AudioLayer> aliAudioLayers = new ArrayList<>();
        /*设置BlendList(视频locationId)*/
        ArrayList<String> blendList = new ArrayList<>();
        /*设置MixList(音频locationId)*/
        ArrayList<String> mixList = new ArrayList<>();
        StringBuilder locationPrefix = new StringBuilder("RV0");
        for (int i = 0; i < videoLayers.size(); i++) {

            //添加视频布局
            blendList.add(videoLayers.get(i).getLocationId());
            AddCasterLayoutRequest.VideoLayer videoLayer = new AddCasterLayoutRequest.VideoLayer();
            // 设置视频归一化坐标
            ArrayList<Float> positionNormalized = new ArrayList<>();
            positionNormalized.add(videoLayers.get(i).getPositionNormalizeds().get(0));// 设置视频归一水平化坐标
            positionNormalized.add(videoLayers.get(i).getPositionNormalizeds().get(1));// 设置视频归一垂直化坐标
            videoLayer.setHeightNormalized(videoLayers.get(i).getHeightNormalized());  // 设置视频归一化高度比例
            videoLayer.setPositionNormalizeds(positionNormalized);
            videoLayer.setPositionRefer("topLeft"); // 设置视频坐标原点参考系
            aliVideoLayers.add(videoLayer);

            //添加音频布局
            mixList.add(audioLayers.get(i).getLocationId());
            AddCasterLayoutRequest.AudioLayer audioLayer = new AddCasterLayoutRequest.AudioLayer();
            audioLayer.setVolumeRate(audioLayers.get(i).getVolumeRate()); // 设置音频音量倍数
            audioLayer.setValidChannel(audioLayers.get(i).getValidChannel()); // 设置音频输入声道
            aliAudioLayers.add(audioLayer);
        }

        AddCasterLayoutRequest addCasterLayoutRequest = new AddCasterLayoutRequest();
        addCasterLayoutRequest.setCasterId(casterId);
        addCasterLayoutRequest.setBlendLists(blendList);
        addCasterLayoutRequest.setMixLists(mixList);
        addCasterLayoutRequest.setVideoLayers(aliVideoLayers); // 设置VideoLayers
        addCasterLayoutRequest.setAudioLayers(aliAudioLayers); // 设置AudioLayers

        client.sendRequest(addCasterLayoutRequest);
    }

    public void modifyCasterLayout(String casterId, List<VideoLayer> videoLayers, List<AudioLayer> audioLayers) throws LiveException {

        ModifyCasterLayoutRequest request = new ModifyCasterLayoutRequest();
        request.setCasterId("XXXXXX");
        request.setLayoutId("XXXXXX");
        // 视频layer
        List<ModifyCasterLayoutRequest.VideoLayer> videoLayersList = new ArrayList<>();
        ModifyCasterLayoutRequest.VideoLayer videoLayer1 = new ModifyCasterLayoutRequest.VideoLayer();
        //Videolayer配置列表，元素为视频画面的配置信息 - 高度。
        videoLayer1.setHeightNormalized(0.5F);

        //Videolayer配置列表，元素为视频画面的配置信息 - 宽度。
        videoLayer1.setWidthNormalized(0.5F);

        //Videolayer配置列表，元素为视频画面的配置信息 - 位置。
        videoLayer1.setPositionNormalizeds(Arrays.asList(new Float[]{0F, 0F}));

        //Videolayer配置列表，元素为视频画面的配置信息 - 参考坐标。（topLeft、topRight、bottomLeft、 bottomRight、center、topCenter、 bottomCenter、leftCenter、rightCenter）
        videoLayer1.setPositionRefer("topLeft");
        videoLayersList.add(videoLayer1);

        ModifyCasterLayoutRequest.VideoLayer videoLayer2 = new ModifyCasterLayoutRequest.VideoLayer();
        videoLayer2.setHeightNormalized(0.5F);
        videoLayer2.setWidthNormalized(0.5F);
        videoLayer2.setPositionRefer("topLeft");
        videoLayer2.setPositionNormalizeds(Arrays.asList(new Float[]{0.5F, 0F}));
        videoLayersList.add(videoLayer2);

        ModifyCasterLayoutRequest.VideoLayer videoLayer3 = new ModifyCasterLayoutRequest.VideoLayer();
        videoLayer3.setHeightNormalized(0.5F);
        videoLayer3.setWidthNormalized(0.5F);
        videoLayer3.setPositionRefer("topLeft");
        videoLayer3.setPositionNormalizeds(Arrays.asList(new Float[]{0.2F, 0.5F}));
        videoLayersList.add(videoLayer3);
        request.setVideoLayers(videoLayersList);

        //====================================== 音频layer==========================================

        List<ModifyCasterLayoutRequest.AudioLayer> audioLayerList = new ArrayList<>();
        ModifyCasterLayoutRequest.AudioLayer audioLayer1 = new ModifyCasterLayoutRequest.AudioLayer();
        //Audiolayer配置列表，元素为音频配置信息 - 音量。
        audioLayer1.setVolumeRate(1F);
        //Audiolayer配置列表，元素为音频配置信息 - 输入声道(取值范围：leftChannel、rightChannel、all-->默认)
        audioLayer1.setValidChannel("all");
        audioLayerList.add(audioLayer1);

        ModifyCasterLayoutRequest.AudioLayer audioLayer2 = new ModifyCasterLayoutRequest.AudioLayer();
        audioLayer2.setVolumeRate(1F);
        audioLayer2.setValidChannel("all");
        audioLayerList.add(audioLayer2);

        ModifyCasterLayoutRequest.AudioLayer audioLayer3 = new ModifyCasterLayoutRequest.AudioLayer();
        audioLayer3.setVolumeRate(1F);
        audioLayer3.setValidChannel("all");
        audioLayerList.add(audioLayer3);

        request.setAudioLayers(audioLayerList);
        //资源位置locationId关联列表，与videoLayers顺序保持一致。
        request.setBlendLists(Arrays.asList(new String[]{"RV01", "RV02", "RV03"}));
        //资源位置locationId关联列表，与audioLayers顺序保持一致。
        request.setMixLists(Arrays.asList(new String[]{"RV01", "RV02", "RV03"}));

        client.sendRequest(request);
    }


    /**
     * 查询导播台布局信息
     *
     * @param casterId
     */
    public DescribeCasterLayoutsResponse getCasterLayout(String casterId) throws LiveException {

        DescribeCasterLayoutsRequest request = new DescribeCasterLayoutsRequest();
        request.setCasterId(casterId);

        return client.getAcsResponse(request);
    }


    /**
     * 启动导播台
     *
     * @param casterId
     * @throws LiveException
     */
    public void startCaster(String casterId) throws LiveException {

        StartCasterRequest request = new StartCasterRequest();
        request.setCasterId(casterId);

        client.sendRequest(request);
    }

    /**
     * 停止导播台
     *
     * @param casterId
     * @throws LiveException
     */
    public void stopCaster(String casterId) throws LiveException {

        StopCasterRequest request = new StopCasterRequest();
        request.setCasterId(casterId);
        client.sendRequest(request);
    }


    /**
     * 删除导播台
     */
    public void deleteCaster(String casterId) throws LiveException {

        DeleteCasterRequest request = new DeleteCasterRequest();
        request.setCasterId(casterId);
        client.sendRequest(request);

    }

    /**
     * 添加组件
     *
     * @param casterId
     * @param sceneId
     * @param componentIds
     * @param layoutId
     */
    public void addScene(String casterId, String sceneId, List<String> componentIds, String layoutId) {

    }

    /**
     * 启动场景
     *
     * @param casterId
     * @param sceneId
     */
    public void startScene(String casterId, String sceneId) throws LiveException {
        StartCasterSceneRequest request = new StartCasterSceneRequest();
        request.setCasterId(casterId);
        request.setSceneId(sceneId);
        client.sendRequest(request);
    }

    /**
     * 更新导播场景配置
     *
     * @param casterId
     * @param layoutId
     * @param sceneId
     * @param componentsIds
     */
    public void updateCasterScene(String casterId, String layoutId, String sceneId, List<String> componentsIds) throws LiveException {

        UpdateCasterSceneConfigRequest request = new UpdateCasterSceneConfigRequest();

        request.setCasterId(casterId);
        request.setLayoutId(layoutId);
        request.setSceneId(sceneId);
        request.setComponentIds(componentsIds);

        client.sendRequest(request);
    }

    /**
     * 删除场景配置
     *
     * @param casterId
     * @param sceneId
     */
    public void deleteCasterScene(String casterId, String sceneId) {

        DeleteCasterSceneConfigRequest request = new DeleteCasterSceneConfigRequest();

        request.setCasterId(casterId);
        request.setSceneId(sceneId);
        request.setType("Component");
    }


    /**
     * 将PVW布局信息设置到PGM场景，将画面从PVW 切换到 PGM
     *
     * @param casterId
     * @param fromSceneId
     * @param toSceneId
     */
    public void switchPGM(String casterId, String fromSceneId, String toSceneId) {

        CopyCasterSceneConfigRequest copyCasterSceneConfigRequest = new CopyCasterSceneConfigRequest();
        copyCasterSceneConfigRequest.setCasterId(casterId);
        copyCasterSceneConfigRequest.setFromSceneId(fromSceneId);
        copyCasterSceneConfigRequest.setToSceneId(toSceneId);
    }


    /**
     * 预览开启
     *
     * @param csterId
     * @param sceneId
     */
    public void StartPVW(String csterId, String sceneId) throws LiveException {

        StartCasterSceneRequest request = new StartCasterSceneRequest();
        request.setCasterId(csterId);
        request.setSceneId(sceneId);

        client.sendRequest(request);
    }

    /**
     * 预览关闭
     *
     * @param casterId
     * @param sceneId
     */
    public void stopPVW(String casterId, String sceneId) throws LiveException {

        StopCasterSceneRequest request = new StopCasterSceneRequest();
        request.setCasterId(casterId);
        request.setSceneId(sceneId);

        client.sendRequest(request);
    }

}
