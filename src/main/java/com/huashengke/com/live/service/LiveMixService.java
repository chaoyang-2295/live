/*
package com.huashengke.com.live.service;

import com.huashengke.com.live.body.LiveRoom;
import com.huashengke.com.live.body.LiveRoomStatus;
import com.huashengke.com.live.body.LiveStream;
import com.huashengke.com.live.dao.NewLiveDao;
import com.huashengke.com.tools.cache.LiveCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

//数据流业务服务

@Service
public class LiveMixService{

    @Autowired
    private NewLiveDao liveDao;
    @Autowired
    private LiveCache liveCache;
    @Autowired
    private LiveQueryService queryService;
    @Autowired
    private AliLiveRequestService aliLiveService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //添加新的流
  public void addNewStream(String liveId, String stream, String description) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if(liveRoom.findStream(stream)!=null){
            throw new Exception("该直播已经存在流"+stream);
        }
        aliLiveService.finishLiveRequest(liveRoom.getAppName(), stream);
        liveDao.addNewStream(liveId, stream, description);
        liveRoom.addNewStream(stream, description);
    }

    //修改流描述
    public void changeStreamDescription(String liveId, String stream, String description) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        liveDao.changeStreamDescription(liveId,stream,description);
        liveRoom.changeStreamDescription(stream, description);
    }

    //禁用流
    public void forbidStream(String liveId, String stream) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if (stream.equals(liveRoom.getStreamName())) {
            throw new Exception("请在直播列表进行对主麦的操作");
        }
        LiveStream liveStream = liveRoom.safeFindStream(stream);
        if (liveStream.isForbid()) {
            throw new Exception("该流正处于禁止状态");
        }
        aliLiveService.finishLiveRequest(liveRoom.getAppName(), stream);
        liveDao.changeStreamForbid(liveId, stream,1);
        liveStream.setForbid(true);
    }

    public void openStream(String liveId, String stream) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if (stream.equals(liveRoom.getStreamName())) {
            throw new Exception("请在直播列表进行对主麦的操作");
        }
        LiveStream liveStream = liveRoom.safeFindStream(stream);
        if (!liveStream.isForbid()) {
            throw new Exception("该流正处于开启状态");
        }
        aliLiveService.startLiveRequest(liveRoom.getAppName(), stream);
        liveDao.changeStreamForbid(liveId, stream,0);
        liveStream.setForbid(false);
    }
    
    //显示流
    public void showStream(String liveId, String stream) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if (stream.equals(liveRoom.getStreamName())) {
            throw new Exception("请在直播列表进行对主麦的操作");
        }
        if (liveRoom.getShowNumber() >= 4) {
            throw new Exception("该直播间目前混流数大于等于4不能继续添加混流");
        }
        LiveStream liveStream = liveRoom.safeFindStream(stream);
        if (liveStream.isShow()) {
            throw new Exception("该流正处于显示状态");
        }
        if (liveRoom.isPush() && liveStream.isPush()) {
            aliLiveService.safeAddStream(liveRoom.getAppName(), liveRoom.getStreamName(), liveRoom.getAppName(), stream);
        }
        aliLiveService.startLiveRequest(liveRoom.getAppName(), stream);
        liveDao.changeStreamShow(liveId, stream,1);
        liveStream.setShow(true);
    }

    //隐藏流
    public void hiddenStream(String liveId, String stream) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if (stream.equals(liveRoom.getStreamName())) {
            throw new Exception("请在直播列表进行对主麦的操作");
        }
        LiveStream liveStream = liveRoom.safeFindStream(stream);
        if (!liveStream.isShow()) {
            throw new Exception("该流正处于隐藏状态");
        }
        if (liveRoom.isPush() && liveStream.isPush()) {
            aliLiveService.removeMixStream(liveRoom.getAppName(), liveRoom.getStreamName(), liveRoom.getAppName(), stream);
        }
        aliLiveService.startLiveRequest(liveRoom.getAppName(), stream);
        liveDao.changeStreamShow(liveId, stream,0);
        liveStream.setShow(false);
    }

    public void fixLiveShow(String liveId) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if(!liveRoom.getStatus().equals(LiveRoomStatus.LIVING)||!liveRoom.isPush()) {
            return;
        }
        String app = liveRoom.getAppName();
        String stream = liveRoom.getStreamName();
        List<LiveStream> showStreams = liveRoom.getShowStream();
        for (LiveStream removeStream : showStreams) {
            aliLiveService.removeMixIgnoreNotStartError(app, stream, app, removeStream.getStream());
        }
        aliLiveService.safeStartMix(app,stream);
        for (LiveStream removeStream : showStreams) {
            aliLiveService.safeAddStream(app, stream, app, removeStream.getStream());
        }
    }

    public void changeMainStream(String liveId, String newStream) throws Exception {
        LiveRoom liveRoom = liveCache.get(liveId);
        if(newStream.equals(liveRoom.getStreamName())){
            return;
        }
        LiveStream newLiveStream = liveRoom.findStream(newStream);
        if (!newLiveStream.isShow()) {
            throw new Exception("未显示状态的流无法切换成主麦");
        }
        String app = liveRoom.getAppName();
        //如果目前处于直播中状态则会访问第三方来修改直播画面
        if (liveRoom.getStatus().equals(LiveRoomStatus.LIVING)) {
            if (!newLiveStream.isPush()) {
                throw new Exception("直播过程中,未推流的流无法切换成主麦");
            }
            List<LiveStream> showStreams = liveRoom.getShowStream();
            //如果当前直播间正在推流中则先处理旧主麦的混流
            LiveStream currentStream;
            if (liveRoom.isPush()) {
                //从旧主麦的画面中移除副麦(忽略NotStart异常)
                for (int i = 0; i < showStreams.size(); i++) {
                    currentStream = showStreams.get(i);
                    if (!currentStream.isMain()) {
                        aliLiveService.removeMixIgnoreNotStartError(app, liveRoom.getStreamName(), app, currentStream.getStream());
                    }
                }
                aliLiveService.stopMixIgnoreNotStartError(app, liveRoom.getStreamName());
            }
            //新主麦开启连麦并将其他副麦加入画面
            aliLiveService.safeStartMix(app, newStream);
            for (int i = 0; i < showStreams.size(); i++) {
                currentStream = showStreams.get(i);
                if (!newStream.equals(currentStream.getStream())) {
                    aliLiveService.safeAddStream(app, newStream, app, currentStream.getStream());
                }
            }
        }
        //发起请求修改聚合根数据库中的状态
        liveDao.changeMainStream(liveId,newStream, liveRoom.getStreamName());
    }

    public void pushStream(String action, String appname, String id) {
        try {
            LiveStream stream = queryService.getLiveStreamByAppName(appname, id);
            LiveRoom liveRoom = liveCache.get(stream.getLiveId());
            if (action.equals("publish")) {
                if(stream.isMain()) {
                    liveDao.changeMainStreamPush(liveRoom.getId(),id,1);
                    stream.setPush(true);
                }else {
                    liveDao.changeStreamPush(liveRoom.getId(),id,1);
                    stream.setPush(true);
                }
                //如果不是混流推流则不做处理，仅仅改变数据库状态
                if (liveRoom.getDefinition().charAt(0) == 'm') {
                    if (stream.isMain()) {
                        aliLiveService.safeStartMix(liveRoom.getAppName(), stream.getStream());
                        Collection<LiveStream> addStreams = liveRoom.getShowStream();
                        //如果找到了混流开启的流是主麦并且正在推流则将需要加入混流的流加进去
                        for (LiveStream addStream : addStreams) {
                            if(!addStream.getStream().equals(stream.getStream())) {
                                aliLiveService.safeAddStream(liveRoom.getAppName(), stream.getStream(), liveRoom.getAppName(), addStream.getStream());
                            }
                        }
                    } else if (liveRoom.isPush()&&stream.isShow()) {
                        aliLiveService.safeAddStream(liveRoom.getAppName(), liveRoom.getStreamName(), liveRoom.getAppName(), stream.getStream());
                    }
                }

            } else if (action.equals("publish_done")) {
                if(stream.isMain()) {
                    liveDao.changeMainStreamPush(liveRoom.getId(),id,0);
                    stream.setPush(false);
                }else {
                    liveDao.changeStreamPush(liveRoom.getId(),id,0);
                    stream.setPush(false);
                }
                //如果所有该app下的流都已中断则关闭混流可用状态

                //如果是混流而且是副麦，则将连麦移出
                if (liveRoom.getDefinition().charAt(0) == 'm') {
                    if (!stream.isMain()) {
                        if (liveRoom.isPush()) {
                            aliLiveService.removeMixStream(liveRoom.getAppName(), liveRoom.getStreamName(), liveRoom.getAppName(), id);
                        }
                    } else {
                        Collection<LiveStream> removeStreams = liveRoom.getShowStream();
                        for (LiveStream removeStream : removeStreams) {
                            aliLiveService.removeMixIgnoreNotStartError(liveRoom.getAppName(), stream.getStream(), liveRoom.getAppName(), removeStream.getStream());
                        }
                        aliLiveService.stopMixIgnoreNotStartError(liveRoom.getAppName(), liveRoom.getStreamName());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("ali push stream callback error:"+action, e);
        }
    }
}
*/
