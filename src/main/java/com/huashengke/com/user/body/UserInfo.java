package com.huashengke.com.user.body;

import com.huashengke.com.live.body.Sex;

import java.util.Date;

/**
 * Created by yangc on 2018/4/27.
 */
public class UserInfo {

    private String userId;
    private String username;
    private String password;
    private Sex sex;
    private String qq;
    private String email;
    private String wechat;
    private   String avatar;
    private String telephone;
    private String realname;
    private Date birthday;
    private String accid;
    private String token;
    private Date createdOn;
    private int fansCount;
    private int followCount;

    public UserInfo() {
    }

    public String getPassword() {
        return password;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getUserId() {
        return userId;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getUsername() {
        return username;
    }

    public String getRealname() {
        return realname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Sex getSex() {
        return sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public int getFansCount() {
        return fansCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public String getQq() {
        return qq;
    }

    public String getWechat() {
        return wechat;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
