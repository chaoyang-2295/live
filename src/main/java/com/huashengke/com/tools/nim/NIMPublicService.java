package com.huashengke.com.tools.nim;

import com.huashengke.com.tools.StringUtil;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class NIMPublicService {

    private DataSource ds;
    private NIMService nimService;

    public NIMPublicService(DataSource dataSource) {
        super();
        this.ds = dataSource;
        nimService = new NIMService();
    }

    /**
     * 创建聊天室
     * @return
     */
    public NIMChatroom createChatRoom(String creatorAccId, String title, String announcement){

        return nimService.createChatRoom(creatorAccId, title, announcement);
    }

    /**
     * 注册网易账号
     * @return
     */
    public NIMRegisterResponse register(String userId) {

        NIMUser user = getNimUser(userId);
        if (user == null) {
            return null;
        }
        if (!StringUtil.isStringEmpty(user.getAccid())) {
            return new NIMRegisterResponse(new RegisterInfo(user.getAccid(), user.getUserName(), user.getToken()));
        }
        String accId = getAccId(user.getAutoId());
        NIMRegisterResponse response = nimService.register(accId, user.getUserName());
        if (response.getCode() != 200) {
            return response;
        }
        //将申请好的网易云账号保存到对象中
        saveAccid(user.getUserId(), accId, response.getInfo().getToken());
        return response;
    }

    private void saveTeamChat(String accId, String otherId, String tid, TeamChatType type, String token) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement("insert into tbl_vw_team_nim_id (acc_id, other_id, tid, `type`, token) VALUES (?,?,?,?,?)");
            statement.setString(1, accId);
            statement.setString(2, otherId);
            statement.setString(3, tid);
            statement.setString(4, type.name());
            statement.setString(5, token);
            statement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param userId
     * @param accid
     * @param token
     */
    private void saveAccid(String userId, String accid, String token) {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("UPDATE `user_info` SET `nim_accid`=?,`nim_token`=? WHERE id=?")) {
                statement.setString(1, accid);
                statement.setString(2, token);
                statement.setString(3,userId);
                statement.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *  通过传入的autoId生成网易聊天室id
     * @param autoId
     * @return   u + autoId
     */
    private String getAccId(int autoId) {
        return "u" + autoId;
    }

    /**
     * 获取用户网易账号信息
     * @param userId
     * @return
     */
    public NIMUser getNimUser(String userId){
        NIMUser nimUser = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = ds.getConnection();
            statement = conn.prepareStatement("SELECT `id` auto_id,`user_id`," +
                    "case `cert_allow_public` when 1 then `cert_realname` else `realname` end name," +
                    "`avatar`,`nim_accid` accid,`nim_token` token,nim_app_key FROM `tbl_vw_user` WHERE `user_id`=?");
            statement.setString(1, userId);
            resultSet =  statement.executeQuery();
            if ( resultSet.next() ){
                nimUser = new NIMUser();
                nimUser.setAutoId( resultSet.getInt( "auto_id" ) );
                nimUser.setUserId( resultSet.getString( "user_id" ) );
                nimUser.setUserName( resultSet.getString( "name" ) );
                nimUser.setAvatar( resultSet.getString( "avatar" ) );
                nimUser.setAccid( resultSet.getString( "accid" ) );
                nimUser.setToken( resultSet.getString( "token" ) );
                nimUser.setNimAppKey( resultSet.getString( "nim_app_key" ) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(conn,statement,resultSet);
        }
        return nimUser;
    }

    /**
     * 关闭数据库连接
     * @param conn
     * @param stmt
     * @param rs
     */
    public void close(Connection conn,PreparedStatement stmt,ResultSet rs ){
        try{
            if(rs != null){
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            if(conn != null){
                conn.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
