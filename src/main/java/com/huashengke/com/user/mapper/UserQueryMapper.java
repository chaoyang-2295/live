package com.huashengke.com.user.mapper;

import com.huashengke.com.user.body.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by chentz on 2018/1/31.
 */
@Mapper
@Component
public interface UserQueryMapper {

    @Select("select * from tbl_vw_user where user_id=#{user_id}")
    UserDetail getUserDetail(@Param("user_id") String userId);

    @Select("select * from tbl_vw_user where telephone = #{username} or email = #{username}")
    UserDetail getUserByTelephoneOrEmail(@Param("username") String username);

    @Select("select * from tbl_vw_user where telephone = #{telephone}")
    UserDetail getUserDetailByTelephone(@Param("telephone") String telephone);

    @Select("select id from tbl_vw_user where username=#{username}")
    String getUserAutoIdByUserName(@Param("username") String userName);

    @Select("SELECT id FROM tbl_vw_user WHERE email=#{email} AND status != 'DELETED' AND is_confirm_email='1'")
    String getUserAutoIdByEmail(@Param("email") String email);

    @Select("select id from tbl_vw_user where telephone=#{telephone}")
    String getUserAutoIdByTelehone(@Param("telephone") String telephone);

    @Select("select status from tbl_vw_invite_code where invite_code=#{inviteCode}")
    String getInviteCodeStatus(@Param("inviteCode") String inviteCode);

    @Select("select id from major where name = #{name}")
    String getMajorIdByMajorName(@Param("name") String majorName);

    @Select("select id from univs where universityName = #{school}")
    String getSchoolIdBySchool(@Param("school") String school);

    @Select("select id from company where name = #{company}")
    String getCompanyIdByCompanyName(@Param("company") String company);

    @Select("select id from duty where name = #{duty}")
    String getDutyIdByDutyName(@Param("duty") String duty);

    @Select("select id from vocation where name = #{name}")
    String getVocationIdByVocationName(@Param("name") String vocationName);

    @Select("select user_id from tbl_vw_user where qq_openid=#{openId}")
    String getUserIdByQQOpenId(@Param("openId") String openId);

    @Select("select user_id from tbl_vw_user where email=#{email} and is_confirm_email=1")
    String getUserIdByEmail(@Param("email") String email);

    @Select("select user_id from tbl_vw_user where wechat_openid=#{openId}")
    String getUserIdByWechatOpenId(@Param("openId") String openId);

    @Select("select user_id from tbl_vw_user where telephone=#{telephone}")
    String getUserIdByTelephone(@Param("telephone") String telephone);

    @Select("select user_id from tbl_vw_user where username=#{username}")
    String getUserIdByUserName(@Param("username") String userName);

    @Select("SELECT user_id FROM tbl_vw_user WHERE id=#{id}")
    String getUserIdByAutoId(@Param("id") int id);

    @Select("select user_id from tbl_vw_user where realname=#{realName}")
    Set<String> getUserIdsByRealname(@Param("realName") String realname);

    @Select("select id from tbl_vw_user where realname=#{username}")
    Set<String> getUserAutoIdsByRealName(@Param("username") String username);

    @Select("select user_id from tbl_vw_user where industry_id=#{industryId} order by id desc limit #{begin},#{length}")
    Set<String> getVocationMembersByIndustryId(@Param("industryId") int industryId, @Param("begin") int begin, @Param("length") int length);

    @Select("SELECT user_id FROM tbl_vw_user_follow_technical_point t1 " +
            "JOIN `tbl_vw_technical_points` t2 ON t1.t_point_id=t2.t_point_id WHERE t1.user_id=#{userId} AND t2.name=#{tPointName}")
    String getUserIdByTpointName(@Param("userId") String userId, @Param("tPointName") String tPointName);

    @Select("SELECT id FROM tbl_vw_user_shield WHERE user_id=#{userId} AND shielded_id=#{shieldedId} AND shielded_type=#{shieldType}")
    String getUserShieldAutoId(@Param("userId") String userId, @Param("shieldedId") String relatedId, @Param("shieldType") int shieldId);

    @Select("select user_id from tbl_vw_requirement where requirement_id = #{requirementId}")
    String getRequirementUseId(@Param("requirementId") String requirementId);

    @Select("SELECT clear FROM tbl_vw_user_interest_by_user WHERE user_id=#{userId} AND other_id=#{interestedId} AND type=#{type}")
    String getUserInterestClear(@Param("userId") String userId, @Param("interestedId") String interestedId, @Param("type") int type);

    @Select("SELECT realname FROM tbl_vw_user WHERE realname=#{realName}")
    String getRealName(@Param("realName") String realName);

    @Select("SELECT id FROM tbl_vw_user where user_id = #{userId}")
    String getAutoIdByUserId(@Param("userId") String userId);

    @Select("SELECT realname FROM tbl_vw_user WHERE realname=#{realName} AND user_id!= #{userId}")
    String getRealNameByUserId(@Param("userId") String userId, @Param("realName") String realName);


    @Select("SELECT 1 FROM tbl_vw_user WHERE username=? AND `status`='NONACTIVATED'")
    String getUserStatus(@Param("username") String username);

}

