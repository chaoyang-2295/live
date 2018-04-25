package com.huashengke.com.tools.nim;

import com.huashengke.com.tools.ObjectSerializer;
import com.huashengke.com.tools.PropertiesUtil;
import com.huashengke.com.tools.UUIDUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

@Service
public class NIMPostService {

    private String appKey;
    private String appSecret;
    private HttpClientUtil httpClientUtil;

    public NIMPostService(){
        Properties properties = PropertiesUtil.getProperties("nim.properties");
        this.appKey = properties.getProperty("nimAppKey");
        this.appSecret = properties.getProperty("nimAppSecret");
        httpClientUtil = new HttpClientUtil();
    }

    /**
     *  向网易云服务器发送请求
     * @param url  请求路径
     * @param params  请求参数
     * @param cls     返回类型class对象
     * @param <T>
     * @return
     */
    public <T> T postNIMServer(String url, List<NameValuePair> params, Class<T> cls) {
        String rst = postNIMServer(url.trim(), params);

        //将返回的结果反序列化为对象
        return ObjectSerializer.instance().deserialize(rst, cls);
    }

    public String postNIMServer(String url, List<NameValuePair> params) {
        //UTF-8编码,解决中文问题
        HttpEntity entity;

        try {
            entity = new UrlEncodedFormEntity(params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return postNIMServer(url, entity);
    }

    public String postNIMServer(String url, final HttpEntity entity) {
        HttpPost post = httpClientUtil.createPost(url, entity, null);

        // addHeader
        HttpClientUtil.addHeader(post, "AppKey", appKey);
        String nonce = UUIDUtil.getUUID();
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        HttpClientUtil.addHeader(post, "Nonce", nonce);
        HttpClientUtil.addHeader(post, "CurTime", curTime);
        String checksum = EncodeUtil.getCheckSum(appSecret,nonce, curTime);
        HttpClientUtil.addHeader(post, "CheckSum", checksum);

        // logger
        //logger.info("Nonce {} | CurlTime {} | CheckSum {}", new Object[]{nonce, curTime, checksum});

        return httpClientUtil.fetchData(post);
    }

    public boolean checkRequest(HttpServletRequest request) {
        try {
            // 获取请求体
            byte[] body = readBody(request);
            if (body == null) {
                return false;
            }
            // 获取部分request header，并打印
            String curTime = request.getHeader("CurTime");
            String md5 = request.getHeader("MD5");
            String checkSum = request.getHeader("CheckSum");
            // 将请求体转成String格式，并打印
            String requestBody = new String(body, "utf-8");
            // 获取计算过的md5及checkSum
            String verifyMD5 = EncodeUtil.getMD5(requestBody);
            String verifyChecksum = EncodeUtil.getCheckSum(appSecret, verifyMD5, curTime);
            if(verifyMD5.equals(md5)&&verifyChecksum.equals(checkSum)){
                return true;
            }else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    private byte[] readBody(HttpServletRequest request) throws IOException {
        if (request.getContentLength() > 0) {
            byte[] body = new byte[request.getContentLength()];
//            IOUtils.readFully(request.getInputStream(), body);
            return body;
        } else
            return null;
    }

}
