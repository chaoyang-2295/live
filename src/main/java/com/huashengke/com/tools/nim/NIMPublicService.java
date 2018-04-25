package com.huashengke.com.tools.nim;

import com.google.gson.Gson;
import com.huashengke.com.tools.StringUtil;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Service
public class NIMPublicService extends NIMService {
    private String defaultName;
    private String defaultAvatar;
    private DataSource ds;

    public NIMPublicService(DataSource dataSource) {
        super();
        this.ds = dataSource;
        Properties properties = new Properties();
        InputStream in = NIMPublicService.class.getClassLoader().getResourceAsStream("nim.properties");
        try {
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.defaultName = properties.getProperty("defaultName");
        this.defaultAvatar = properties.getProperty("defaultAvatar");
    }

    public NIMCreateTeamResponse createTeamChat(String otherName, int autoId, String otherId, List<String> membersId, TeamChatType type) {
        String accId = type.getAccId(autoId);
        if(otherName.length()>30){
            otherName = otherName.substring(0,28);
            otherName = otherName.concat("……");
        }
        NIMRegisterResponse response = super.register(accId, defaultName, defaultAvatar);
        if (response == null || response.getCode() != 200) {
            response = super.updateToken(accId);
            if(response == null || response.getCode() != 200) {
                return response==null?new NIMCreateTeamResponse(500,"noR"):new NIMCreateTeamResponse(response.getCode(), response.getDesc());
            }
        }
        Gson gson = new Gson();
        NIMCreateTeamResponse teamResponse = this.createTeam(otherId,otherName, accId, gson.toJson(membersId,List.class));
        if (teamResponse.getCode() != 200) {
            return teamResponse;
        }
        saveTeamChat(accId, otherId, teamResponse.getTid(), type, response.getInfo().getToken());
        return teamResponse;
    }

    public NIMRegisterResponse register(NIMUser user) {
        if (user == null) {
            return null;
        }
        if (!StringUtil.isStringEmpty(user.getAccid())) {
            return new NIMRegisterResponse(new RegisterInfo(user.getAccid(), user.getUserName(), user.getToken()));
        }

        String avatar = null;
        if (StringUtil.isStringEmpty(user.getAvatar())) {
//            avatar = OSSServiceProvider.getInstance().getPublicURIByLocalKey(user.getAvatar());
        }
        //生成accid
        String accId = getAccId(user.getAutoId());
        NIMRegisterResponse response = super.register(accId, user.getUserName(), avatar);
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
}
