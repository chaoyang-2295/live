package com.huashengke.com.live.service;

import com.aliyuncs.live.model.v20161101.*;
import com.huashengke.com.live.AliClient;
import com.huashengke.com.live.body.MediaType;
import com.huashengke.com.tools.Md5Util;
import com.huashengke.com.tools.StringUtil;
import com.huashengke.com.tools.exception.live.LiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AliLiveRequestService {

    @Autowired
    private AliClient client;
    
    private static final String MIX_TEMPLATE = "pip4a";

    private static final int OUTDATE_TIME = 5*60;

    public void finishLiveRequest(String appName,String streamName) throws LiveException {
        ForbidLiveStreamRequest request = new ForbidLiveStreamRequest();
        request.setAppName(appName);
        request.setStreamName(streamName);
        request.setLiveStreamType("publisher");
        request.setDomainName(client.getDomain());
        client.sendRequest(request);
    }

    public void startMix(String appName,String streamName) throws LiveException {
        StartMultipleStreamMixServiceRequest startRequest = new StartMultipleStreamMixServiceRequest();
        startRequest.setDomainName(client.getDomain());
        startRequest.setAppName(appName);
        startRequest.setStreamName(streamName);
        startRequest.setMixTemplate(MIX_TEMPLATE);
        client.sendRequest(startRequest);
    }

    public void stopMix(String appName,String streamName) throws LiveException {
        StopMultipleStreamMixServiceRequest stopRequest = new StopMultipleStreamMixServiceRequest();
        stopRequest.setDomainName(client.getDomain());
        stopRequest.setAppName(appName);
        stopRequest.setStreamName(streamName);
        client.sendRequest(stopRequest);
    }

    public void addMixStream(String appName,String streamName,String mixAppName,String mixStreamName) throws LiveException {
        AddMultipleStreamMixServiceRequest addRequest = new AddMultipleStreamMixServiceRequest();
        addRequest.setDomainName(client.getDomain());
        addRequest.setAppName(appName);
        addRequest.setStreamName(streamName);
        addRequest.setMixDomainName(client.getDomain());
        addRequest.setMixAppName(mixAppName);
        addRequest.setMixStreamName(mixStreamName);
        client.sendRequest(addRequest);
    }

    public void removeMixStream(String appName,String streamName,String mixAppName,String mixStreamName) throws LiveException {
        RemoveMultipleStreamMixServiceRequest removeRequest = new RemoveMultipleStreamMixServiceRequest();
        removeRequest.setDomainName(client.getDomain());
        removeRequest.setAppName(appName);
        removeRequest.setStreamName(streamName);
        removeRequest.setMixDomainName(client.getDomain());
        removeRequest.setMixAppName(mixAppName);
        removeRequest.setMixStreamName(mixStreamName);
        client.sendRequest(removeRequest);
    }

    public void startLiveRequest(String appName,String streamName) throws LiveException {
        ResumeLiveStreamRequest request = new ResumeLiveStreamRequest();
        request.setAppName(appName);
        request.setStreamName(streamName);
        request.setLiveStreamType("publisher");
        request.setDomainName(client.getDomain());
        client.sendRequest(request);
    }

    public void safeStartMix(String appName, String stream) throws LiveException {
        stopMixIgnoreNotStartError(appName, stream);
        this.startMix(appName, stream);
    }

    public void removeMixIgnoreNotStartError(String appName, String stream, String mixAppName, String mixstream) throws LiveException {
        try {
            removeMixStream(appName, stream, mixAppName, mixstream);
        } catch (LiveException e) {
            if (!e.getMessage().contains("NotStart")) {
                throw e;
            }
        }
    }

    public void addLiveMixConfig(String app, String template) throws LiveException{
        AddLiveMixConfigRequest addRequest = new AddLiveMixConfigRequest();
        addRequest.setDomainName(client.getDomain());
        addRequest.setAppName(app);
        addRequest.setTemplate(template);
        client.sendRequest(addRequest);
    }

    public void deleteLiveMixConfig(String app) throws LiveException{
        DeleteLiveMixConfigRequest deleteRequest = new DeleteLiveMixConfigRequest();
        deleteRequest.setDomainName(client.getDomain());
        deleteRequest.setAppName(app);
        client.sendRequest(deleteRequest);
    }

    public void addLiveStreamTranscode(String appName, String definition) throws LiveException {
        AddLiveStreamTranscodeRequest request = new AddLiveStreamTranscodeRequest();
        request.setDomain(client.getDomain());
        request.setApp(appName);
        request.setTemplate(definition);

        try {
            client.sendRequest(request);
        }catch (LiveException e){
            if(e.getMessage().contains("ConfigAlreadyExists")){
                return;
            }else{
                throw e;
            }
        }

    }

    public void deleteLiveStreamTranscode(String appName, String definition) throws LiveException {
        DeleteLiveStreamTranscodeRequest deleteRequest = new DeleteLiveStreamTranscodeRequest();
        deleteRequest.setDomain(client.getDomain());
        deleteRequest.setApp(appName);
        deleteRequest.setTemplate(definition);
        client.sendRequest(deleteRequest);
    }

    public DescribeLiveStreamTranscodeInfoResponse getLiveMixConfig() throws LiveException {
        DescribeLiveStreamTranscodeInfoRequest queryRequest = new DescribeLiveStreamTranscodeInfoRequest();
        queryRequest.setDomainTranscodeName(client.getDomain());
        client.sendRequest(queryRequest);
        return client.getAcsResponse(queryRequest);
    }

    public List<String> getMixConfigByAppName(String appName) throws LiveException {
        DescribeLiveMixConfigRequest queryRequest = new DescribeLiveMixConfigRequest();
        queryRequest.setDomainName(client.getDomain());
        client.sendRequest(queryRequest);
        DescribeLiveMixConfigResponse response = client.getAcsResponse(queryRequest);
        return response.getMixConfigList().stream()
                .filter(mixConfig -> mixConfig.getAppName().equals(appName))
                .map(mixConfig -> mixConfig.getTemplate())
                .collect(Collectors.toList());
    }

    public void startRecordByAppName(String appName) throws LiveException {

        //查询域名下录制配置列表
        DescribeLiveRecordConfigRequest recordConfigRequest = new DescribeLiveRecordConfigRequest();
        recordConfigRequest.setDomainName(client.getDomain());          //加速域名
        recordConfigRequest.setAppName(appName);                        //应用名称
        client.sendRequest(recordConfigRequest);
        DescribeLiveRecordConfigResponse recordConfigResponse = client.getAcsResponse(recordConfigRequest);
        if (recordConfigResponse.getLiveAppRecordList().size() > 0) {   //已经开始录制
            return;
        }

        //添加APP录制配置、视屏存储位置、存储格式
        AddLiveAppRecordConfigRequest request = new AddLiveAppRecordConfigRequest();
        //加速域名
        request.setDomainName(client.getDomain());
        //直播流所属的应用名
        request.setAppName(appName);
        //oos存储bucket名
        request.setOssBucket(client.getLiveBucket());
        //Endpoint 表示 OSS 对外服务的访问域名
        request.setOssEndpoint(client.getEndPoint());

        //配置录制文件格式信息
        AddLiveAppRecordConfigRequest.RecordFormat format = new AddLiveAppRecordConfigRequest.RecordFormat();
        //配置录制视屏格式
        format.setFormat("mp4");
        //oos的存储录制文件名,参数值必须要有 {StartTime} 或 {EscapedStartTime} 和 {EndTime} 或 {EscapedEndTime}
        format.setOssObjectPrefix(client.getVideoName());
        List<AddLiveAppRecordConfigRequest.RecordFormat> formats = new ArrayList<>();
        formats.add(format);
        request.setRecordFormats(formats);
        client.sendRequest(request);
    }

    public void stopLiveRecordByAppName(String appName) throws LiveException {
        DeleteLiveAppRecordConfigRequest request = new DeleteLiveAppRecordConfigRequest();
        request.setDomainName(client.getDomain());
        request.setAppName(appName);
        client.sendRequest(request);
    }

    public void safeAddStream(String appName, String stream, String mixAppName, String mixstream) throws LiveException {
        removeMixIgnoreNotStartError(appName, stream, mixAppName, mixstream);
        addMixStream(appName, stream, mixAppName, mixstream);
    }

    public void stopMixIgnoreNotStartError(String appName, String stream) throws LiveException{
        try {
            stopMix(appName, stream);
        } catch (LiveException e) {
            if (!e.getMessage().contains("NotStart")) {
                throw e;
            }
        }
    }

    public String getDomain(){
        return client.getDomain();
    }

    public String getPrivateKey(){
        return client.getPrivateKey();
    }

    public String getUrl(String app,String stream){
        return client.getDomain()+"/"+app+"/"+stream;
    }

    public String getLiveAuthenticationKey(String app, String stream, MediaType mediaType, String definition){
        long failureTime = System.currentTimeMillis()/1000+OUTDATE_TIME;
        StringBuilder sb = new StringBuilder();
        sb.append("/").append(app)
                .append("/")
                .append(stream)
                .append(StringUtil.isStringEmpty(definition)?"":"_")
                .append(definition)
                .append(mediaType.getMediaStr())
                .append("-")
                .append(failureTime)
                .append("-0-0-")
                .append(getPrivateKey());
        String encryptUrlkey = Md5Util.makeMd5Sum(sb.toString().getBytes());
        return failureTime+"-0-0-"+encryptUrlkey;
    }

    public String getLivePushAuthenticationKey(String app, String stream){
        long failureTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("/").append(app)
                .append("/")
                .append(stream)
                .append("-")
                .append(failureTime)
                .append("-0-0-")
                .append(getPrivateKey());
        String encryptUrlkey = Md5Util.makeMd5Sum(sb.toString().getBytes());
        return failureTime+"-0-0-"+encryptUrlkey;
    }


    public String getCourseCreator(){
        return client.getCourseCreator();
    }
}
