package com.huashengke.com.tools.nim;

import com.huashengke.com.tools.ObjectSerializer;
import com.huashengke.com.tools.PropertiesUtil;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
public class NIMPostService {

    private String appKey;
    private String appSecret;

    private static final Logger logger = LoggerFactory.getLogger(NIMService.class);

    public NIMPostService(){
        Properties properties = PropertiesUtil.getProperties("nim.properties");
        this.appKey = properties.getProperty("nimAppKey");
        this.appSecret = properties.getProperty("nimAppSecret");
    }

    /**
     *  向网易云服务器发送请求
     * @param url     请求路径
     * @param params  请求参数
     * @param cls     返回类型class对象
     * @param <T>
     * @return
     */
     <T> T postNIMServer(String url, List<NameValuePair> params, Class<T> cls) {

        String curTime;
        String checksum;
        String result = null;
        String nonce = getUUID();
        HttpPost httpPost = new HttpPost(url);

        CloseableHttpResponse response;
        CloseableHttpClient httpClient;
        try {

            httpClient = HttpClients.createDefault();
            curTime = String.valueOf(System.currentTimeMillis() / 1000);
            checksum = EncodeUtil.getCheckSum(appSecret,nonce, curTime);
            //============================AddHeader=================================
            httpPost.addHeader("AppKey", appKey);
            httpPost.addHeader("Nonce", nonce);
            httpPost.addHeader("CurTime", curTime);
            httpPost.addHeader("CheckSum", checksum);
            httpPost.addHeader("User-Agent","Netease/0.1");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //设置参数
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

            response = httpClient.execute(httpPost);
            result =  fetchData(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ObjectSerializer.instance().deserialize(result, cls);
    }


     String fetchData(HttpResponse response) throws IOException {

        String result = null;
        long watch = System.nanoTime();

            HttpEntity rsEntity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Charset charset = ContentType.getOrDefault(rsEntity).getCharset();
                if (charset != null && charset.name().equals("ISO-8859-1")) {
                    result = EntityUtils.toString(rsEntity);
                    //转码为UTF-8
                    result = new String(result.getBytes(charset), "utf-8");
                } else {
                    result = EntityUtils.toString(rsEntity, "utf-8");
                }
            } else {
                logger.error("fetch request return error status: code:{" + response.getStatusLine().getStatusCode() + "}");
            }
        return result;
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


    private String getUUID(){
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
}
