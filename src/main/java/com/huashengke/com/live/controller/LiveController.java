package com.huashengke.com.live.controller;

import com.huashengke.com.live.body.*;
import com.huashengke.com.live.service.LiveService;
import com.huashengke.com.tools.Result;
import com.huashengke.com.tools.exception.HuaShengKeException;
import com.huashengke.com.tools.exception.live.LiveException;
import com.huashengke.com.tools.exception.user.UserException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class LiveController {

    @Autowired
    private LiveService liveService;


    @ResponseBody
    @ApiOperation("创建直播间")
    @RequestMapping(value = "/createLiveRoom", method = RequestMethod.POST)
    public Result<?> createLive(@RequestBody LiveRoomCreateBody liveData) {
        try {

            LiveRoom liveRoom = liveService.createLiveRoom( liveData );
            return Result.result(liveRoom);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    @ApiOperation("创建直播")
    @RequestMapping(value = "/createLive", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> createLive(@RequestBody LiveBody liveBody) {
        try {

            Live live = liveService.createLive(liveBody);
            return Result.result(live);
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    @ApiOperation("开始直播,进行直播流的推送")
    @RequestMapping(value="/startLive", method = RequestMethod.PUT)
    @ResponseBody
    public Result<?> startLive(@RequestParam String liveRoomId){
        try {

            return Result.result(liveService.startLive(liveRoomId));
        } catch (LiveException e) {
            return  Result.error(e.getRc(), e.getMessage());
        }
    }

    @ApiOperation("开始直播,进行直播流的推送")
    @RequestMapping(value="/stopLive", method = RequestMethod.PUT)
    @ResponseBody
    public Result<?> stopLive(@RequestParam String liveRoomId){
        try {
            liveService.stopLive(liveRoomId);
            return Result.ok();
        } catch (LiveException e) {
            return  Result.error(e.getRc(), e.getMessage());
        }
    }


    @ApiOperation("修改直播间信息")
    @RequestMapping(value = "/changeLiveRoom", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> changeLive(@RequestBody LiveRoomChangeBody body) {
        try {
            liveService.changeLiveRoom(body);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }

    @ApiOperation("修改直播状态")
    @RequestMapping(value = "/changeLiveStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> changeLiveStatus(@RequestParam("liveRoomId") String liveRoomId,
                                        @RequestParam("status") LiveStatus status) {
        try {
            liveService.changeLiveStatus(liveRoomId, status);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }


   /* @ApiOperation("转码配置,转码模版，目前有标准质量模板：lld、lsd、lhd、lud，高品质（窄带高清转码）模板：ld、sd、hd、ud之间用&连接")
    @RequestMapping(value = "/transCoderConfig", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> transCoderConfig(@RequestParam("definition") String definition,
                                      @RequestParam("liveId") String liveId) {
        try {
            liveService.transCodeConfig(definition, liveId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }*/


    @ApiOperation("开始视频录制")
    @RequestMapping(value = "/startRecord", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> startRecord(@RequestParam("liveId") String liveId) {

        try {
            liveService.startRecord(liveId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }


    /**
     * 停止录制
     * @param liveId 直播间id
     */
    @ApiOperation("停止视频录制")
    @RequestMapping(value = "/stopRecord", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> stopRecord(@RequestParam("liveId") String liveId) {
        try {
            liveService.stopRecord(liveId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }

    @ApiOperation("添加新的流")
    @RequestMapping(value = "/addNewStream", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> addNewStream(@RequestParam("liveId") String liveRoomId,
                                  @RequestParam("streamName") String streamName,
                                  @RequestParam("description") String description) {
        try {
           liveService.addNewStream(liveRoomId, streamName, description);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }

    @ApiOperation("开始直播流推送")
    @RequestMapping(value = "/showStream", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> showStream(@RequestParam("liveId") String liveId,
                                @RequestParam("streamName") String streamName) {
        try {
            liveService.pushStream(liveId, streamName);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }

    @ApiOperation("隐藏流")
    @RequestMapping(value = "/hiddenStream", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> hiddenStream(@RequestParam("liveId") String liveId,
                                  @RequestParam("streamName") String streamName) {
        try {
//            liveMixService.hiddenStream(liveId, streamName);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }

    @ApiOperation("封禁流")
    @RequestMapping(value = "/forbidStream", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> forbidStream(@RequestParam("liveId") String liveId,
                                  @RequestParam("streamName") String streamName) {
        try {
//            liveMixService.forbidStream(liveId, streamName);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }

    @ApiOperation("开启流")
    @RequestMapping(value = "/openStream", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> openStream(@RequestParam("liveId") String liveId,
                                @RequestParam("streamName") String streamName) {
        try {
//            liveMixService.openStream(liveId, streamName);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }


    @ApiOperation("更换成主流")
    @RequestMapping(value = "/changeMainStream", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> changeMainStream(@RequestParam("liveId") String liveId,
                                      @RequestParam("streamName") String streamName) {
        try {
//            liveMixService.changeMainStream(liveId, streamName);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }





    @ApiOperation("修改流描述")
    @RequestMapping(value = "/changeStreamDescription", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> changeStreamDescription(@RequestParam("liveId") String liveId,
                                             @RequestParam("streamName") String streamName,
                                             @RequestParam("description") String description) {
        try {
//            liveMixService.changeStreamDescription(liveId, streamName, description);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }


    @ApiOperation("推流")
    @RequestMapping("/pushStream")
    public Result<?> pushStream(@RequestParam("action") String action,
                                @RequestParam("appname") String appname,
                                @RequestParam("id") String id) {
//        liveMixService.pushStream(action, appname, id);
        return Result.ok();
    }
/*
    @ApiOperation("阿里云回调接口")
    @RequestMapping("/recordCallback")
    @ResponseBody
    public Result<?> recordSuccess(@RequestBody RecordCallbackBody recordFileMakeBody) {
        liveService.recordCallback(recordFileMakeBody);
        return Result.ok();
    }*/
    @ApiOperation("")
    @RequestMapping("/fixLiveShow")
    @ResponseBody
    public Result<?> fixLiveShow(@RequestParam String liveId) {
        try {
//            liveMixService.fixLiveShow(liveId);
        } catch (Exception e) {
            return Result.error(1, e.getMessage());
        }
        return Result.ok();
    }
}
