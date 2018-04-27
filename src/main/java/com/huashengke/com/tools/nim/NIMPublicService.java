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
     */
    public NIMChatroom createChatRoom(String creatorAccId, String title, String announcement){

        return nimService.createChatRoom(creatorAccId, title, announcement);
    }

    /**
     * 注册网易账号
     */
    public NIMRegisterResponse register(String userId) {

        NIMUser user = getNimUser(userId);
        if (user == null) {
            return null;
        }
        if (!StringUtil.isStringEmpty(user.getAccid())) {
            return new NIMRegisterResponse(new RegisterInfo(user.getAccid(), user.getNickname(), user.getToken()));
        }
        String accId = getAccId(user.getId());
        NIMRegisterResponse response = nimService.register(accId, user.getNickname());
        if (response.getCode() != 200) {
            return response;
        }
        saveNimInfo(user.getUserId(), accId, response.getInfo().getToken());
        return response;
    }


    private void saveNimInfo(String userId, String accid, String token) {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("UPDATE tbl_vw_user SET nim_accid=?,nim_token = ? WHERE user_id=?")) {
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
     */
    private String getAccId(int autoId) {
        return "accId_" + autoId;
    }

    /**
     * 获取用户网易账号信息
     */
    NIMUser getNimUser(String userId){
        NIMUser nimUser = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = ds.getConnection();
            statement = conn.prepareStatement("SELECT id, user_id, nickname, nim_accid, nim_token, nim_app_key FROM tbl_vw_user where user_id = ?");
            statement.setString(1, userId);
            resultSet =  statement.executeQuery();
            if ( resultSet.next() ){
                nimUser = new NIMUser();
                nimUser.setId( resultSet.getInt( "id" ) );
                nimUser.setUserId( resultSet.getString( "user_id" ) );
                nimUser.setNickname( resultSet.getString( "nickname" ) );
                nimUser.setAccid( resultSet.getString( "nim_accid" ) );
                nimUser.setToken( resultSet.getString( "nim_token" ) );
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
