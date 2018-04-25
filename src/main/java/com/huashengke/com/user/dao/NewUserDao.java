package com.huashengke.com.user.dao;

import com.huashengke.com.live.body.UserInfo;
import com.huashengke.com.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;


@Service
public class NewUserDao {

    @Autowired
    private UserMapper userMapper;

    public UserInfo getUserInfo(String userInfo ){

        return userMapper.getUserInfo( userInfo );
    }

    public void logLogin( String userId){
        Timestamp nowDate = new Timestamp(System.currentTimeMillis());
        userMapper.loginLogInsert(userId,nowDate);
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
        userMapper.insertUserInfo(userInfo.getUserId(),userInfo.getUsername(),userInfo.getPassword(),userInfo.getEmail(),userInfo.getTelephone(),userInfo.getSex().name(),nowDate);
        return null;
    }

    
    public String changeUserDescription(String description,int archiverScore, String userId){

        userMapper.changeDescription( userId,description,archiverScore );
        return userId;
    }
    
    public String addUserDescription(String expertDescription, String userId){

        userMapper.addExpertDescription(userId,expertDescription);
        return userId;
    }


    public  String followUser( String userId, String followedUserId, boolean isFollow){

        Timestamp nowDate = new Timestamp(System.currentTimeMillis());
        userMapper.updateUserAttentionCount( userId ,1);
        userMapper.updateUserFansCount( followedUserId,1 );
        if( isFollow )
            userMapper.insertUserFollowByUserInfo(userId,followedUserId,nowDate );
        else
            userMapper.deleteUserFollowByUserInfo(userId,followedUserId );
        return userId;
    }

    public String followUsers( String userId, List<String> followedUserIds, boolean isFollow){
        Timestamp nowDate = new Timestamp(System.currentTimeMillis());
        if( isFollow){
            userMapper.updateUserAttentionCount(userId,followedUserIds.size()  );
            for(int i = 0; i < followedUserIds.size() ;i++){
                userMapper.insertUserFollowByUserInfo( userId,followedUserIds.get(i),nowDate );
                userMapper.updateUserFansCount( followedUserIds.get( i ),1 );
            }
        }else{
//            userMapper.deleteUserFollowByUserInGivenUser(userId, com.netflix.discovery.util.StringUtil.join( followedUserIds.toArray( new String[followedUserIds.size() ] ) ) );
            userMapper.updateUserAttentionCount(userId,-followedUserIds.size());
            for (int i= 0;i < followedUserIds.size();i++){

                userMapper.updateUserFansCount(followedUserIds.get( i ),-1);
            }
        }
        return userId;
    }

    
    public String defriend( String userId, String friendUserId){
        Timestamp nowDate = new Timestamp(System.currentTimeMillis());
        userMapper.defriend(userId,friendUserId,nowDate);
        return userId;
    }

    
    public String unfriend( String userId, String friendUserId){

        userMapper.unfriend(userId,friendUserId);
        return userId;
    }

    
    public String setManager( String userId, boolean isManager){

        userMapper.setManager(userId,isManager ? 1:0 );
        return userId;
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

        userMapper.changeUserStatus(userId,status );
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
