package com.huashengke.com.user.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
@Mapper
@Component
public interface UserMapper {

    @Insert("insert into user_info(user_id,username, password, email,telephone,sex,created_on)  VALUES (#{user_id},#{username},#{password},#{email},#{telephone},#{sex},#{created_on})")
    void insertUserInfo(@Param("user_id") String userId, @Param("password") String password, @Param("email") String email, @Param("telephone") String telephone, @Param("created_on") Date createdOn);

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

    @Insert("insert into tbl_vw_user_gag (user_id, status, occured_on, end_time, manager_id) VALUES (#{user_id},#{status},#{occured_on},#{end_time},#{manager_id}) " +
            "ON DUPLICATE KEY UPDATE status=#{status},occured_on=#{occured_on},end_time=#{end_time},manager_id=#{manager_id}")
    void beSilent(@Param("user_id") String userId, @Param("status") int status, @Param("occured_on") Date occured_on, @Param("end_time") Date endTime, @Param("manager_id") String managerId);

    @Delete("delete from tbl_vw_user_gag where user_id=#{user_id}")
    void cancelSilent(@Param("user_id") String userId);

    @Update("update tbl_vw_user set qq_openid=#{qq_open_id} where user_id=#{user_id}")
    void bindQQ(@Param("user_id") String userId, @Param("qq_open_id") String qqOpenId);

    @Update("update tbl_vw_user set wechat_openid=#{wechat_open_id} where user_id=#{user_id}")
    void bindWechat(@Param("user_id") String userId, @Param("wechat_open_id") String wechatOpenId);
}
