package com.huashengke.com.live.controller;

import com.huashengke.com.live.body.*;
import com.huashengke.com.live.service.LiveService;
import com.huashengke.com.tools.Result;
import com.huashengke.com.tools.exception.live.LiveException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class LiveController {

    @Autowired
    private LiveService liveService;
//    @Autowired
//    private LiveMixService liveMixService;


    @ResponseBody
    @ApiOperation("创建直播间")
    @RequestMapping(value = "/createLiveRoom", method = RequestMethod.POST)
    public Result<?> createLive(@RequestBody LiveRoomCreateBody liveData) {
        try {
            liveService.createLiveRoom( liveData );
            return Result.ok();
        } catch (Exception e) {
            return Result.error( -1 );
        }
    }


    @ApiOperation("创建直播")
    @RequestMapping(value = "/createLive", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> createLiveChapter(@RequestBody LiveBody liveBody) {
        try {
            liveService.createLive(liveBody);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    /*@ResponseBody
    @ApiOperation("修改直播章节")
    @RequestMapping(value = "/changeLiveChapter", method = RequestMethod.POST)
    public Result<?> changeLiveChapter(@RequestBody LiveChapterChangeBody changeBody) {
        try {
            liveService.changeLiveChapter(changeBody);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    @ResponseBody
    @ApiOperation("删除直播章节")
    @RequestMapping(value = "/deleteChapter",method = RequestMethod.POST)
    public Result<?> deleteChapter(@RequestParam("chapterId")String chapterId,
                                   @RequestParam("liveId")String liveId){
        try {
            liveService.deleteChapter(chapterId,liveId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(),e.getMessage());
        }
    }

    @ResponseBody
    @ApiOperation("修改视频源")
    @RequestMapping(value = "/changeChapterVideo", method = RequestMethod.POST)
    public Result<?> changeChapterVideo(@RequestParam("liveId") String liveId,
                                        @RequestParam("chapterId") String chapterId,
                                        @RequestParam("videoId") String videoId) {
        try {
            liveService.changeChapterVideo(liveId, chapterId, videoId);
            return Result.ok();
        } catch (LiveException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }*/


    @ApiOperation("修改直播信息")
    @RequestMapping(value = "/changeLive", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> changeLive(@RequestBody LiveChangeBody liveChangeBody) {
        try {
            liveService.changeLive(liveChangeBody);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }

    @ApiOperation("修改直播状态")
    @RequestMapping(value = "/changeLiveStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> changeLiveStatus(@RequestParam("liveId") String liveId,
                                        @RequestParam("status") LiveRoomStatus status) {
        try {
            liveService.changeLiveStatus(liveId, status);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(-1, e.getMessage());
        }
    }


    @ApiOperation("转码配置,转码模版，目前有标准质量模板：lld、lsd、lhd、lud，高品质（窄带高清转码）模板：ld、sd、hd、ud之间用&连接")
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
    }


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
     * @return
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

    @ApiOperation("开始直播流推送")
    @RequestMapping(value = "/showStream", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> showStream(@RequestParam("liveId") String liveId,
                                @RequestParam("streamName") String streamName) {
        try {
//            liveMixService.showStream(liveId, streamName);
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


    @ApiOperation("添加新的流")
    @RequestMapping(value = "/addNewStream", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> addNewStream(@RequestParam("liveId") String liveId,
                                  @RequestParam("streamName") String streamName,
                                  @RequestParam("description") String description) {
        try {
//            liveMixService.addNewStream(liveId, streamName, description);
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
