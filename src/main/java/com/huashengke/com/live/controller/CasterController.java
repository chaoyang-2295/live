package com.huashengke.com.live.controller;

import com.huashengke.com.live.body.CasterCallBackBody;
import com.huashengke.com.live.body.CasterCreateBody;
import com.huashengke.com.live.body.CasterLayoutBody;
import com.huashengke.com.live.body.CasterVideoBody;
import com.huashengke.com.live.service.CasterService;
import com.huashengke.com.tools.Result;
import com.huashengke.com.tools.exception.live.LiveException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yangc on 2018/4/13.
 */

@Api("阿里导播台相关服务")
@RestController
public class CasterController {

    @Autowired
    private CasterService casterService;

    /**
     * 创建导播台
     *
     */
    @RequestMapping(value = "/createCaster", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> createCaster(@RequestBody CasterCreateBody body) {
        try {

            casterService.createCaster(body);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 添加布局
     *
     */
    @RequestMapping(value = "/addLayout", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> addLayout(@RequestBody CasterLayoutBody body) {
        try {

            casterService.addCasterLayout(body);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 添加视频源
     *
     */
    @RequestMapping(value = "/addVideo", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> addVideo(@RequestBody CasterVideoBody body) {
        try {

            casterService.addCasterVideo(body);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }


    /**
     *启动导播台
     *
     */
    @RequestMapping(value = "/startCaster", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> startCaster(@RequestParam String casterId) {
        try {

            casterService.startCaster(casterId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }


    /**
     * 启动场景
     *
     */
    @RequestMapping(value = "/startScene", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> startScene(@RequestParam String casterId, String sceneId) {
        try {

            casterService.startScene(casterId, sceneId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 停止导播台
     *
     */
    @RequestMapping(value = "/stopCaster", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> stopCaster(@RequestParam String casterId) {
        try {

            casterService.stopCaster(casterId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 修改场景
     *
     */
    @RequestMapping(value = "/changeScene", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> changeScene(@RequestParam String casterId, @RequestParam String sceneId) {
        try {
            casterService.modifyCasterScene(casterId, sceneId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 修改视频源
     *
     */
    @RequestMapping(value = "/changeVideo", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> changeVideo(@RequestBody CasterVideoBody body) {
        try {
            casterService.modifyCasterVideo(body);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 删除场景
     *
     * @return
     */
    @RequestMapping(value = "/deleteScene", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> deleteScene(@RequestParam String casterId, @RequestParam String sceneId) {
        try {

            casterService.deleteCasterScene(casterId, sceneId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 删除视频源
     *
     * @return
     */
    @RequestMapping(value = "/deleteVideo", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> deleteVideo(@RequestParam String casterId, @RequestParam String resourceId) {
        try {

            casterService.deleteCasterVideo(casterId, resourceId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 删除导播台
     *
     */
    @RequestMapping(value = "/deleteCaster", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> deleteCaster(@RequestParam String casterId) {
        try {

            casterService.deleteCaster(casterId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }


    /**
     * 添加回调
     *
     * @return
     */
    @RequestMapping(value = "/casterCallBack", method = RequestMethod.POST)
    @ResponseBody
    public void casterCallBack(@RequestBody CasterCallBackBody body) {

        casterService.receiveCallBack(body);

    }

}

