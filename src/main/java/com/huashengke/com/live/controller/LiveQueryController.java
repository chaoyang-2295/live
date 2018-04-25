package com.huashengke.com.live.controller;


import com.huashengke.com.live.body.Definition;
import com.huashengke.com.live.body.LiveContentBody;
import com.huashengke.com.live.body.MediaType;
import com.huashengke.com.live.service.LiveQueryService;
import com.huashengke.com.live.service.LiveService;
import com.huashengke.com.tools.Result;
import com.huashengke.com.tools.exception.live.LiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chentz on 2017/11/9.
 */
@Controller
public class LiveQueryController {
    @Autowired
    private LiveQueryService queryService;

    @Autowired
    private LiveService liveService;

    /**
     * 获取直播初始页面
     *
     * @return
     */
    @RequestMapping(value = "/getLiveInitialBody", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getLiveInitialBody(@RequestParam("liveId") String liveId,
                                        @RequestParam("userId") String userId) {
        try {
            LiveContentBody body = queryService.getLiveContentBody(liveId, userId);
            return Result.result(body);
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 获取直播播放页面
     *
     * @param liveId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getLivePlayBody", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getLivePlayBody(@RequestParam("liveId") String liveId,
                                     @RequestParam("userId") String userId,
                                     @RequestParam(value = "mediaType", defaultValue = "RTMP") MediaType mediaType) {
        try {
            return Result.result(queryService.getLivePlayBody(liveId, userId,mediaType));
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /**
     * 获取直播状态
     *
     * @return
     */
    @RequestMapping(value = "/getLiveStatusData", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getLiveStatusData(@RequestParam("liveId") String liveId,
                                     @RequestParam(value = "mediaType", defaultValue = "RTMP") MediaType mediaType,
                                     @RequestParam(value = "definition",required = false)Definition definition) {
        try {
            return Result.result(queryService.getLiveStatusData(liveId, mediaType,definition));
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }


    @RequestMapping(value = "/getLiveAuthenticationData", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getLiveAuthenticationData(@RequestParam("app") String app,@RequestParam("stream") String stream) {
      //  return Result.result(liveService.getLiveAuthenticationKey(app,stream));
        return Result.ok();
    }
}
