package com.huashengke.com.user.service;

import com.huashengke.com.tools.VerifyUtil;
import com.huashengke.com.tools.cache.UserCache;
import com.huashengke.com.tools.exception.user.UserErrorRc;
import com.huashengke.com.tools.exception.user.UserException;
import com.huashengke.com.user.body.UserDetail;
import com.huashengke.com.user.mapper.UserQueryMapper;
import com.huashengke.com.user.body.UserRegisterBody;
import com.huashengke.com.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by yangc on 2018/4/26.
 */
@Service
public class UserService {

    @Autowired
    private UserCache userCache;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserQueryMapper queryMapper;



    public UserDetail register(String ip, UserRegisterBody body) throws UserException {

        String email = VerifyUtil.isEmail(body.getUsername());
        String telephone = VerifyUtil.isTelephone(body.getUsername());

        if(queryMapper.getUserByTelephoneOrEmail(body.getUsername()) != null){
            throw new UserException("账户已存在！", UserErrorRc.AccountAlreadyExist);
        }

        String userId = generateUserId();
        userMapper.insertUserInfo(userId, VerifyUtil.encryptedValue(body.getPassword()), email, telephone, body.getNickname(), new Date());
        return new UserDetail(userId, email, telephone, body);
    }

    public UserDetail login(String username, String password) throws UserException{

       UserDetail userDetail = queryMapper.getUserByTelephoneOrEmail( username );
       if(userDetail == null){
           throw new UserException( "用户不存在", UserErrorRc.NoSuchUserError );
       }
       if( !VerifyUtil.encryptedValue( password ).equals( userDetail.getPassword() )){
           throw new UserException( "密码错误", UserErrorRc.PasswordError);
       }

        return userDetail;
    }


    public UserDetail loginWithQQ(String openId, String token) throws UserException{

        return null;
    }
    public UserDetail loginWithWeChat(String openId, String token) throws UserException{

        return null;
    }



    public UserDetail bindTelephone(String userId, String telephone, String code) throws UserException{


        return null;
    }


    public UserDetail bindQQ(String userId, String openId, String token) throws UserException{


        return null;
    }


    public UserDetail bindWeChat(String userId, String openId, String token) throws UserException{

        return null;
    }

    public UserDetail registWithWeChat(String openId, String token) throws UserException{

        return null;
    }


    public UserDetail registWithQQ(String openId, String token) throws UserException{

        return null;
    }





    private String generateUserId(){

        return UUID.randomUUID().toString();
    }
}
