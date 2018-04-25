package com.huashengke.com.user.mapper;

import com.huashengke.com.live.body.UserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
@Mapper
@Component
public interface UserMapper {

    @Select("select * from tbl_vw_user where user_id=#{user_id}")
    UserInfo getUserInfo(@Param("user_id") String userId);

    @Insert("insert into user_info(user_id,username, password, email,telephone,sex,created_on)  VALUES (#{user_id},#{username},#{password},#{email},#{telephone},#{sex},#{created_on})")
    void insertUserInfo(@Param("user_id") String userId, @Param("username") String username, @Param("password") String password, @Param("email") String email, @Param("telephone") String telephone, @Param("sex") String sex, @Param("created_on") Date createdOn);

    @Insert("INSERT INTO tbl_vw_user_login_log (user_id, created_on) VALUES(#{user_id},#{created_on})")
    int loginLogInsert(@Param("user_id") String userId, @Param("created_on") Date createdOn);

    @Update("UPDATE tbl_vw_user SET username=#{user_name} WHERE user_id=#{user_id}")
    void changeUserName(@Param("user_id") String userId, @Param("user_name") String userName);

    @Update( "UPDATE tbl_vw_user SET password=#{password} WHERE user_id=#{user_id}" )
    void changePassword(@Param("user_id") String userId, @Param("password") String password);

    @Update("UPDATE tbl_vw_user SET email = #{email},modified_on=#{modified_on},is_confirm_email=0 WHERE user_id = ?")
    void changeEmail(@Param("user_id") String userId, @Param("modified_on") Date modifiedOn, @Param("email") String email);

    @Update( "UPDATE tbl_vw_user SET telephone = #{telephone},modified_on=#{modified_on} WHERE user_id = #{user_id}" )
    void changeTelephone(@Param("user_id") String user_id, @Param("modified_on") Date modified_on, @Param("telephone") String telephone);

    @Update( "UPDATE tbl_vw_user SET is_confirm_email=1 WHERE user_id = #{user_id}" )
    void activateEmail(@Param("user_id") String userId);

    @Update("UPDATE tbl_vw_user SET description = #{description},archive_score=#{archive_score}  WHERE user_id = user_id")
    void changeDescription(@Param("user_id") String userId, @Param("description") String description, @Param("archive_score") int archiveScore);

    @Update("UPDATE tbl_vw_user SET expert_description=#{expert_description} WHERE user_id = #{user_id}")
    void addExpertDescription(@Param("user_id") String userId, @Param("expert_description") String expertDescription);


    @Update("UPDATE tbl_vw_user_profile SET attention_count=attention_count+ #{attention} WHERE user_id= #{user_id}")
    void updateUserAttentionCount(@Param("user_id") String userId, @Param("attention") int attentionCount);

    @Update("UPDATE tbl_vw_user_profile SET fans_count=fans_count+#{fansCount} WHERE user_id=#{user_id}")
    void updateUserFansCount(@Param("user_id") String followUserId, @Param("fansCount") int fansCount);

    @Insert("insert IGNORE into tbl_vw_user_follow_by_user (user_id, followed_user_id, occured_on) VALUES(#{user_id},#{followed_user_id},#{occured_on})")
    void insertUserFollowByUserInfo(@Param("user_id") String userId, @Param("followed_user_id") String followedUserId, @Param("occured_on") Date date);

    @Delete("delete from tbl_vw_user_follow_by_user where user_id=#{user_id} and followed_user_id=#{followed_user_id}")
    void deleteUserFollowByUserInfo(@Param("user_id") String userId, @Param("followed_user_id") String followedUserId);

    @Insert("INSERT INTO tbl_vw_user_black_list(user_id, defriended_user_id,occured_on) VALUES(#{user_id},#{defriended_user_id},#{occured_on})")
    void defriend(@Param("user_id") String userId, @Param("defriended_user_id") String friendUserId, @Param("occured_on") Date occured_on);

    @Delete("DELETE FROM tbl_vw_user_black_list WHERE user_id=#{user_id} AND defriended_user_id=#{defriended_user_id}")
    void unfriend(@Param("user_id") String userId, @Param("defriended_user_id") String friendUserId);

    @Update("UPDATE tbl_vw_user SET is_manager=#{is_manager} WHERE user_id=#{user_id}")
    void setManager(@Param("user_id") String userId, @Param("is_manager") int isManager);

    @Insert("insert into tbl_vw_user_gag (user_id, status, occured_on, end_time, manager_id) VALUES (#{user_id},#{status},#{occured_on},#{end_time},#{manager_id}) " +
            "ON DUPLICATE KEY UPDATE status=#{status},occured_on=#{occured_on},end_time=#{end_time},manager_id=#{manager_id}")
    void beSilent(@Param("user_id") String userId, @Param("status") int status, @Param("occured_on") Date occured_on, @Param("end_time") Date endTime, @Param("manager_id") String managerId);

    @Delete("delete from tbl_vw_user_gag where user_id=#{user_id}")
    void cancelSilent(@Param("user_id") String userId);

    @Update("UPDATE tbl_vw_user SET status =#{status} WHERE user_id=#{user_id}")
    void changeUserStatus(@Param("user_id") String userId, @Param("status") String status);

    @Update("update tbl_vw_user set qq_openid=#{qq_open_id} where user_id=#{user_id}")
    void bindQQ(@Param("user_id") String userId, @Param("qq_open_id") String qqOpenId);

    @Update("update tbl_vw_user set wechat_openid=#{wechat_open_id} where user_id=#{user_id}")
    void  bindWechat(@Param("user_id") String userId, @Param("wechat_open_id") String wechatOpenId);

}
