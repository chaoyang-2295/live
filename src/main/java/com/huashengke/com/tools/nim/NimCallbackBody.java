package com.huashengke.com.tools.nim;

import java.util.Date;

/**
 * Created by chentz on 2017/12/11.
 */
public class NimCallbackBody {
    /**
     * 值为1，表示是会话类型的消息
     */
    String eventType;
    /**
     * 会话具体类型：PERSON（二人会话数据）、TEAM（群聊数据）
     * CUSTOM_PERSON（个人自定义系统通知）、CUSTOM_TEAM（群组自定义系统通知），字符串类型
     */
    String convType;
    /**
     * 若convType为PERSON或CUSTOM_PERSON，则to为消息接收者的用户账号，字符串类型；
     * 若convType为TEAM或CUSTOM_TEAM，则to为tid，即群id，可转为Long型数据
     */
    String to;
    /**
     * 消息发送者的用户账号，字符串类型
     */
    String fromAccount;
    /**
     * 发送客户端类型： AOS、IOS、PC、WINPHONE、WEB、REST，字符串类型
     */
    String fromClientType;
    /**
     * 	发送设备id，字符串类型
     */
    String fromDeviceId;
    /**
     * 发送方昵称，字符串类型
     */
    String fromNick;
    /**
     * 消息发送时间，字符串类型
     */
    long msgTimestamp;
    /**
     * 会话具体类型PERSON、TEAM对应的通知消息类型：
     TEXT、
     PICTURE、
     AUDIO、
     VIDEO、
     LOCATION 、
     NOTIFICATION、
     FILE、 //文件消息
     NETCALL_AUDIO、 //网络电话音频聊天
     NETCALL_VEDIO、 //网络电话视频聊天
     DATATUNNEL_NEW、 //新的数据通道请求通知
     TIPS、 //提醒类型消息
     CUSTOM //自定义消息

     会话具体类型CUSTOM_PERSON对应的通知消息类型：
     FRIEND_ADD、 //加好友
     FRIEND_DELETE、 //删除好友
     CUSTOM_P2P_MSG、 //个人自定义系统通知

     会话具体类型CUSTOM_TEAM对应的通知消息类型：
     TEAM_APPLY、 //申请入群
     TEAM_APPLY_REJECT、 //拒绝入群申请
     TEAM_INVITE、 //邀请进群
     TEAM_INVITE_REJECT、 //拒绝邀请
     TLIST_UPDATE、 //群信息更新
     CUSTOM_TEAM_MSG、 //群组自定义系统通知
     */
    String msgType;
    /**
     * 消息内容，字符串类型
     */
    String body;
    /**
     * 附加消息，字符串类型
     */
    String attach;
    /**
     * 客户端生成的消息id，仅在convType为PERSON或TEAM含此字段，字符串类型
     */
    String msgidClient;
    /**
     * 服务端生成的消息id，可转为Long型数据
     */
    String msgidServer;
    /**
     * 重发标记：0不是重发, 1是重发。仅在convType为PERSON或TEAM时含此字段，可转为Integer类型数据
     */
    String resendFlag;
    /**
     * 自定义系统通知消息是否存离线:0：不存，1：存。仅在convType为CUSTOM_PERSON或CUSTOM_TEAM时含此字段，可转为Integer类型数据
     */
    String customSafeFlag;
    /**
     * 自定义系统通知消息推送文本。仅在convType为CUSTOM_PERSON或CUSTOM_TEAM时含此字段，字符串类型
     */
    String customApnsText;
    /**
     * 跟本次群操作有关的用户accid，仅在convType为TEAM或CUSTOM_TEAM时含此字段，字符串类型。
     * tMembers格式举例：
     {
     // 其他字段
     "tMembers":"[123, 456]" //相关的accid为 123 和 456
     }
     */
    String tMembers;
    /**
     * 消息扩展字段
     */
    String ext;
    /**
     * 标识是否被反垃圾，仅在被反垃圾时才有此字段，可转为Boolean类型数据
     */
    String antispam;

    public String getEventType() {
        return eventType;
    }

    public String getConvType() {
        return convType;
    }

    public String getTo() {
        return to;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getFromClientType() {
        return fromClientType;
    }

    public String getFromDeviceId() {
        return fromDeviceId;
    }

    public String getFromNick() {
        return fromNick;
    }

    public Date getMsgTimestamp() {
        return new Date(msgTimestamp);
    }

    public String getMsgType() {
        return msgType;
    }

    public String getBody() {
        return body;
    }

    public String getAttach() {
        return attach;
    }

    public String getMsgidClient() {
        return msgidClient;
    }

    public String getMsgidServer() {
        return msgidServer;
    }

    public String getResendFlag() {
        return resendFlag;
    }

    public String getCustomSafeFlag() {
        return customSafeFlag;
    }

    public String getCustomApnsText() {
        return customApnsText;
    }

    public String gettMembers() {
        return tMembers;
    }

    public String getExt() {
        return ext;
    }

    public String getAntispam() {
        return antispam;
    }
}
