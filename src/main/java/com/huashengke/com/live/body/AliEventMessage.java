package com.huashengke.com.live.body;

/**
 * Created by yangc on 2018/4/25.
 */
public class AliEventMessage {

    private String code;

    private String message;

    private String requestId;

    private String sceneId;

    private String resourceId;

    private String ComponentId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getComponentId() {
        return ComponentId;
    }

    public void setComponentId(String componentId) {
        ComponentId = componentId;
    }

    @Override
    public String toString() {
        return "AliEventMessage{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", requestId='" + requestId + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", ComponentId='" + ComponentId + '\'' +
                '}';
    }
}
