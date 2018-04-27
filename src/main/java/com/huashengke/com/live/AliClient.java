package com.huashengke.com.live;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.RpcAcsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.huashengke.com.tools.exception.live.LiveErrorRc;
import com.huashengke.com.tools.exception.live.LiveException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class AliClient {

    private SimpleDateFormat format;

    private IAcsClient client;

    private String domain;

    private String privateKey;

    private String courseCreator;

    private String liveBucket;

    private String endPoint;

    private String videoName;

    public AliClient(IAcsClient client, String domain, String privateKey, String courseCreator, String liveBucket, String endPoint, String videoName) {
        this.client = client;
        this.domain = domain;
        this.privateKey = privateKey;
        this.courseCreator = courseCreator;
        this.liveBucket = liveBucket;
        this.endPoint = endPoint;
        this.videoName = videoName;
        this.format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    public String format(Date date) {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }

    public Date parse(String date) throws ParseException {
        return format.parse(date);
    }

    public <T extends AcsResponse> T getAcsResponse(AcsRequest<T> request) throws LiveException {
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            throw new LiveException("阿里请求获取结果失败,错误:"+e.getErrMsg(), LiveErrorRc.AliyunRequstError);
        }
    }

    public HttpResponse sendRequest(RpcAcsRequest request) throws LiveException {
        HttpResponse response;
        try {
            response = client.doAction(request);
        } catch (ClientException e) {
            throw new LiveException("阿里请求发送失败,错误:"+e.getErrMsg(), LiveErrorRc.AliyunRequstError);
        }

        if (response.isSuccess())
            return response;
        else {
            String errorContent = new String(response.getHttpContent());
            throw new LiveException("阿里请求失败,错误:"+errorContent, LiveErrorRc.AliyunRequstError);
        }
    }

    public String getDomain() {
        return domain;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getLiveBucket() {
        return liveBucket;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getCourseCreator() {
        return courseCreator;
    }
}
