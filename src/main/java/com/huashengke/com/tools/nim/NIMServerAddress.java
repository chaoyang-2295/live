package com.huashengke.com.tools.nim;

/**
 * Created by xujunbo on 17-6-23.
 */
public interface NIMServerAddress {
    /**
     * 创建网易云通信ID
     */
    String REGISTER_ACTION = "https://api.netease.im/nimserver/user/create.action";

    /**
     * 更新网易云id的token
     */
    String REFRESH_TOKEN = "https://api.netease.im/nimserver/user/refreshToken.action";
    /**
     * 创建聊天室
     */
    String CREATE_CHATROOM = "https://api.netease.im/nimserver/chatroom/create.action";

    /**
     * 请求聊天室地址
     */
    String REQUEST_CHATROOM_ADDRESS = "https://api.netease.im/nimserver/chatroom/requestAddr.action";

    /**
     * 获取聊天室信息
     */
    String GET_CHATROOM_DATA = "https://api.netease.im/nimserver/chatroom/get.action";

    /**
     * 发送消息
     */
    String SEND_MESSAGE = "https://api.netease.im/nimserver/msg/sendMsg.action";
    /**
     * 创建群
     */
    String CREATE_TEAM = "https://api.netease.im/nimserver/team/create.action";

    /**
     * 查询群
     */
    String QUERY_TEAM = "https://api.netease.im/nimserver/team/query.action";
    /**
     * 修改用户信息
     */
    String UPDATE_USER = "https://api.netease.im/nimserver/user/updateUinfo.action";
}
