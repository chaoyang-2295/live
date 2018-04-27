package com.huashengke.com.user.dao;

import com.huashengke.com.user.body.UserDetail;
import com.huashengke.com.user.body.UserInfo;
import com.huashengke.com.user.mapper.UserMapper;
import com.huashengke.com.user.mapper.UserQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;


@Service
public class NewUserDao {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserQueryMapper queryMapper;

    public UserDetail getUserInfo(String userInfo ){

        return queryMapper.getUserDetail(userInfo);
    }

    public String changeUserName( String userId, String userName){
        userMapper.changeUserName(userId, userName);
        return userId;
    }
    
    public String changePassword( String userId, String password){

        userMapper.changePassword( userId,password );
        return userId;
    }
    
    public String changeEmail( String userId, String email) {

        userMapper.changeEmail( userId, new Date(),email );
        return userId;
    }
    
    public String changeTelephone(String telephone, String userId){

        Timestamp nowDate = new Timestamp( System.currentTimeMillis() );
        userMapper.changeTelephone(userId,nowDate,telephone);
        return userId;
    }
    
    public String activateEmail( String userId){
        userMapper.activateEmail(userId);
        return userId;
    }
    public String registerUser(UserInfo userInfo) throws Exception {

        Timestamp nowDate = new Timestamp(System.currentTimeMillis());
      //  userMapper.insertUserInfo(userInfo.getUserId(),userInfo.getUsername(),userInfo.getPassword(),userInfo.getEmail(),userInfo.getTelephone(),userInfo.getSex().name(),nowDate);
        return null;
    }

    
    public String beSilent( String userId, String managerId, int days){

        Timestamp nowDate = new Timestamp(System.currentTimeMillis());
        int status = days <= 0 ? -1 : 0;
        Date endTime = days <= 0 ? null : new Date(nowDate.getTime()
                + days * 24 * 60 * 60 * 1000);

        userMapper.beSilent(userId,status,nowDate,endTime,managerId);
        return userId;
    }

    
    public String cancelSilent( String userId){

        userMapper.cancelSilent( userId );
        return userId;
    }

    public String changeUserStatus( String userId, String status){

      //  userMapper.changeUserStatus(userId,status );
        return userId;
    }

    
    public  String bindQQ( String userId, String qqOpenId){

        userMapper.bindQQ( userId,qqOpenId);
        return userId;
    }

    
    public String  bindWechat( String userId, String wechatOpenId){

        userMapper.bindWechat(userId,wechatOpenId);
        return userId;
    }
}
