package com.huashengke.com.live.service;

import com.aliyuncs.live.model.v20161101.CreateCasterResponse;
import com.huashengke.com.live.body.*;
import com.huashengke.com.live.mapper.CasterMaper;
import com.huashengke.com.tools.count.CountCache;
import com.huashengke.com.tools.count.CountCacheProvider;
import com.huashengke.com.tools.count.IdCountType;
import com.huashengke.com.tools.exception.live.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yangc on 2018/4/13.
 */
@Service
public class CasterService {

    private CountCache casterIdCounter;

    private CountCache streamIdCounter;

/*    @Autowired
    private CasterMaper casterMaper;*/
    @Autowired
    private AliCasterRequestService aliService;

    private static Logger log = LoggerFactory.getLogger(CasterService.class);

    public CasterService(){
        casterIdCounter = CountCacheProvider.getCountCache(IdCountType.caster);
        streamIdCounter = CountCacheProvider.getCountCache(IdCountType.stream);
    }


    /**
     * 创建导播台
     */
    public void createCaster(CasterCreateBody body) throws LiveException {

        //创建导播台
        CreateCasterResponse response = aliService.createCaster(body.getClientToken(), body.getCasterName());

        //添加导播台配置
        aliService.setCasterConfig(response.getCasterId(), body.getDelay(), body.getUrgentMaterialId());
    }


    /**
     * 添加视频源
     */
    public void addCasterVideo(CasterVideoBody body) throws LiveException {

        aliService.addCasterVideo(body.getCasterId(), body.getLocationId(), body.getResourceName(), body.getLiveStreamUrl());
    }

    /**
     * 删除视频源
     *
     * @param casterId   导播台ID
     * @param resourceId 资源ID
     */
    public void deleteCasterVideo(String casterId, String resourceId) throws LiveException {

        aliService.deleteCasterVideo(casterId, resourceId);
    }

    /**
     * 添加布局
     *
     */
    public void addCasterLayout(CasterLayoutBody body) throws LiveException {

        aliService.addCasterLayout(body.getCasterId(), body.getVideoLayers(), body.getAudioLayers());
    }

    public void modifyCasterLayout(CasterLayoutBody body) throws LiveException {

        aliService.modifyCasterLayout(body.getCasterId(), body.getVideoLayers(), body.getAudioLayers());
    }

    /**
     * 启动导播台
     *
     */
    public void startCaster(String casterId) throws LiveException {

        aliService.startCaster(casterId);
    }

    /**
     * 停止导播台
     *
     */
    public void stopCaster(String casterId) throws LiveException {

        aliService.stopCaster(casterId);
    }

    /**
     * 删除导播台
     */
    public void deleteCaster(String casterId) throws LiveException{

        aliService.deleteCaster(casterId);
    }

    /**
     * 添加组件
     *
     */
    public void addScene(String casterId, String sceneId, List<String> componentIds, String layoutId) {

        aliService.addScene(casterId, sceneId, componentIds, layoutId);
    }

    /**
     * 启动场景
     *
     */
    public void startScene(String casterId, String sceneId) throws LiveException {

        aliService.startScene(casterId, sceneId);
    }

    /**
     * 更新导播场景配置
     *
     */
    public void modifyCasterScene(String casterId, String sceneId) throws LiveException {

    }

    /**
     * 修改导播台视频源
     *
     */
    public void modifyCasterVideo(CasterVideoBody body) throws LiveException {
    }


    /**
     * 删除场景配置
     *
     */
    public void deleteCasterScene(String casterId, String sceneId) throws LiveException {

        aliService.deleteCasterScene(casterId, sceneId);
    }


    /**
     * 将PVW布局信息设置到PGM场景，将画面从PVW 切换到 PGM
     *
     */
    public void switchPGM(String casterId, String fromSceneId, String toSceneId) throws LiveException  {

    }


    /**
     * 预览开启
     *
     */
    public void StartPVW(String csterId, String sceneId) throws LiveException {

    }

    /**
     * 预览关闭
     *
     */
    public void stopPVW(String casterId, String sceneId) throws LiveException {


    }

    /**
     * 记录回调信息
     *
     */
    public void receiveCallBack(CasterCallBackBody body){

        log.info("---------------------CallBack------------------------");
        log.info("-----casterId:"+body.getCasterId());
        log.info("-----eventType:"+body.getEventType());
        log.info("-----EventMessage:"+body.getEventMessage().toString());
    }

}
