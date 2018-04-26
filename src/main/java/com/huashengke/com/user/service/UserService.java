package com.huashengke.com.user.service;

import com.huashengke.com.tools.PasswordEncryptUtil;
import com.huashengke.com.tools.count.CountCache;
import com.huashengke.com.tools.exception.user.UserErrorRc;
import com.huashengke.com.tools.exception.user.UserException;
import com.huashengke.com.user.body.UserDetail;
import com.huashengke.com.user.body.UserQueryMapper;
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
    private UserMapper userMapper;
    @Autowired
    private UserQueryMapper queryMapper;

    public UserDetail register(UserRegisterBody body) throws UserException {

        if(queryMapper.getUserIdByTelephone(body.getTelephone()) != null){
            throw new UserException("手机号已存在！", UserErrorRc.TelephoneHasBound);
        }
        userMapper.insertUserInfo(UUID.randomUUID().toString(), PasswordEncryptUtil.encryptedValue(body.getPassword()), body.getEmail(), body.getTelephone(), new Date());

        return new UserDetail(body);
    }
}
