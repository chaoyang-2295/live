package com.huashengke.com.user.body;

import com.huashengke.com.live.body.Sex;

import java.util.Date;

/**
 * Created by yangc on 2018/4/26.
 */
public class UserDetail {

    private int id;
    private Sex sex;
    private String userId;
    private String telephone;
    private String nickname;
    private Date birthday;
    private String avatar;
    private String password;
    private String email;
    private String wechat;
    private Date createdOn;
    private Date modifiedOn;
    private String qqOpenid;
    private String wechatOpenid;
    private boolean isConfirmEmail;
    private boolean isSilence;

    public UserDetail(){}

    public UserDetail(String userId, String telephone, String email, UserRegisterBody body){

        this.userId = userId;
        this.isSilence = false;
        this.isConfirmEmail = false;
        this.email = email;
        this.telephone = telephone;
        this.nickname = body.getNickname();
        this.wechatOpenid = body.getWeChatOpenId();
        this.qqOpenid = body.getQqOpenId();
    }


    public int getId() {
        return id;
    }

    public Sex getSex() {
        return sex;
    }

    public String getUserId() {
        return userId;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getNickname() {
        return nickname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getWechat() {
        return wechat;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getQqOpenid() {
        return qqOpenid;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public boolean isConfirmEmail() {
        return isConfirmEmail;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public boolean isSilence() {
        return isSilence;
    }
}