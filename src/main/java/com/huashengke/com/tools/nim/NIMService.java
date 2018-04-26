package com.huashengke.com.tools.nim;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

public class NIMService {

    private NIMPostService nimPostService;

    public NIMService() {
        this.nimPostService = new NIMPostService();
    }

    NIMRegisterResponse register(String accid,String name){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("accid", accid));
        params.add(new BasicNameValuePair("name", name));
        return nimPostService.postNIMServer(NIMServerAddress.REGISTER_ACTION, params, NIMRegisterResponse.class);
    }

    public String sendMessage(String from,Integer ope,String to,Integer type,String body){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("from", from));
        params.add(new BasicNameValuePair("ope", ope.toString()));
        params.add(new BasicNameValuePair("to", to));
        params.add(new BasicNameValuePair("type", type.toString()));
        params.add(new BasicNameValuePair("body", body));
        return nimPostService.postNIMServer(NIMServerAddress.SEND_MESSAGE, params,String.class);
    }

    /**
     * 创建聊天室
     *
     * @param creatorAccId 聊天室属主的账号accid
     * @param title        聊天室名称，长度限制128个字符
     * @param announcement 公告，长度限制4096个字符
     */
    NIMChatroom createChatRoom(String creatorAccId, String title,
                                      String announcement) {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("creator", creatorAccId));
        params.add(new BasicNameValuePair("name", title));
        params.add(new BasicNameValuePair("announcement", announcement));
        params.add(new BasicNameValuePair("broadcasturl", null));
        params.add(new BasicNameValuePair("ext", null));

        return nimPostService.postNIMServer(NIMServerAddress.CREATE_CHATROOM, params, NIMChatroom.class);
    }
    /**
     * 获取聊天室信息
     */
    public NIMChatRoomData getNIMRoomData(int roomId){
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("roomid", String.valueOf(roomId)));
        params.add(new BasicNameValuePair("needOnlineUserCount", "true"));
        return nimPostService.postNIMServer(NIMServerAddress.GET_CHATROOM_DATA, params, NIMChatRoomDatas.class).getChatroom();
    }
}
