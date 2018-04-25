package com.huashengke.com.tools.nim;

import com.google.gson.Gson;
import com.huashengke.com.tools.StringUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class NIMService {
    private NIMPostService nimPostService;

    public NIMService() {
        this.nimPostService = new NIMPostService();
    }

    public NIMRegisterResponse register(String accid,String name,String icon){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accid", accid));
        params.add(new BasicNameValuePair("name", name));
        if (StringUtil.isStringEmpty(icon)) {
            params.add(new BasicNameValuePair("icon", icon));
        }
        return nimPostService.postNIMServer(NIMServerAddress.REGISTER_ACTION, params, NIMRegisterResponse.class);
    }

    public NIMCreateTeamResponse createTeam(String otherId,String teamName,String owner,String members){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tname", teamName));
        params.add(new BasicNameValuePair("owner", owner));
        params.add(new BasicNameValuePair("members", members));
        params.add(new BasicNameValuePair("msg", "a"));
        params.add(new BasicNameValuePair("magree", "0"));
        params.add(new BasicNameValuePair("joinmode", "0"));
        params.add(new BasicNameValuePair("intro", otherId));
        params.add(new BasicNameValuePair("beinvitemode","1"));
        params.add(new BasicNameValuePair("uptinfomode","1"));
        params.add(new BasicNameValuePair("upcustommode","1"));
        return nimPostService.postNIMServer(NIMServerAddress.CREATE_TEAM,params,NIMCreateTeamResponse.class);
    }

    public NIMTeamResponse queryTeam(List<String> tid){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Gson gson = new Gson();
        params.add(new BasicNameValuePair("tids", gson.toJson(tid)));
        params.add(new BasicNameValuePair("ope","1"));  //表示带上群成员列表
        return nimPostService.postNIMServer(NIMServerAddress.QUERY_TEAM,params,NIMTeamResponse.class);
    }

    public NIMTeamResponse updateUserInfo(String accId,String icon,String name){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accid", accId));
        if(icon!=null) {
            params.add(new BasicNameValuePair("icon", icon));
        }
        if(name!=null) {
            params.add(new BasicNameValuePair("name", name));
        }
        return nimPostService.postNIMServer(NIMServerAddress.UPDATE_USER,params,NIMTeamResponse.class);
    }

    public NIMRegisterResponse updateToken(String accId){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accid", accId));
        return nimPostService.postNIMServer(NIMServerAddress.REFRESH_TOKEN, params, NIMRegisterResponse.class);
    }

    public String sendMessage(String from,Integer ope,String to,Integer type,String body){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("from", from));
        params.add(new BasicNameValuePair("ope", ope.toString()));
        params.add(new BasicNameValuePair("to", to));
        params.add(new BasicNameValuePair("type", type.toString()));
        params.add(new BasicNameValuePair("body", body));
        return nimPostService.postNIMServer(NIMServerAddress.SEND_MESSAGE, params);
    }

    /**
     * 创建聊天室
     *
     * @param createrAccid 聊天室属主的账号accid
     * @param title        聊天室名称，长度限制128个字符
     * @param announcement 公告，长度限制4096个字符
     * @param broadcasturl 直播地址，长度限制1024个字符
     * @param ext          扩展字段，最长4096字符
     * @return
     */
    public NIMChatroom createChatroom(String createrAccid, String title,
                                      String announcement, String broadcasturl, String ext) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("creator", createrAccid));
        params.add(new BasicNameValuePair("name", title));
        params.add(new BasicNameValuePair("announcement", announcement));
        params.add(new BasicNameValuePair("broadcasturl", broadcasturl));
        params.add(new BasicNameValuePair("ext", ext));

        return nimPostService.postNIMServer(NIMServerAddress.CREATE_CHATROOM, params, NIMChatroom.class);
    }



    public NIMChatRoomData getNIMRoomData(int roomId){
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("roomid", String.valueOf(roomId)));
        params.add(new BasicNameValuePair("needOnlineUserCount", "true"));
        return nimPostService.postNIMServer(NIMServerAddress.GET_CHATROOM_DATA, params, NIMChatRoomDatas.class).getChatroom();
    }

    public NIMRoomAddress requestChatroomAddress(long roomId, String accid, int clienttype) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("roomid", String.valueOf(roomId)));
        params.add(new BasicNameValuePair("accid", accid));
        params.add(new BasicNameValuePair("clienttype", String.valueOf(clienttype)));

        return nimPostService.postNIMServer(NIMServerAddress.REQUEST_CHATROOM_ADDRESS, params, NIMRoomAddress.class);
    }

    public boolean checkCallbackRequest(HttpServletRequest request){
        return nimPostService.checkRequest(request);
    }
}
