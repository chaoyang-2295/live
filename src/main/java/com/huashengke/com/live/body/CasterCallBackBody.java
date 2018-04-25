package com.huashengke.com.live.body;


/**
 * Created by yangc on 2018/4/25.
 */
public class CasterCallBackBody {

    private String casterId;

    private String eventType;

    private AliEventMessage eventMessage;

    public String getCasterId() {
        return casterId;
    }

    public void setCasterId(String casterId) {
        this.casterId = casterId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public AliEventMessage getEventMessage() {
        return eventMessage;
    }

    public void setEventMessage(AliEventMessage eventMessage) {
        this.eventMessage = eventMessage;
    }
}
