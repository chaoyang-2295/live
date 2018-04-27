package com.huashengke.com.user.body;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yangc on 2018/4/26.
 */
public class UserRegisterBody {

    @ApiModelProperty(value = "手机号码")
    private String telephone;

    @ApiModelProperty(value = "用户注册时填写的账号")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "客户端IP")
    private String postIp;

    @ApiModelProperty(value = "注册账号的用户名")
    private String nickname;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "QQ openId")
    private String qqOpenId;

    @ApiModelProperty(value = "微信 openId")
    private String weChatOpenId;

    @ApiModelProperty(value = "QQ或者微信的token")
    private String token;



    public String getTelephone() {
        return telephone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPostIp() {
        return postIp;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    public String getToken() {
        return token;
    }
}
