package com.huashengke.com.user.controller;

import com.huashengke.com.tools.Result;
import com.huashengke.com.tools.exception.user.UserException;
import com.huashengke.com.user.body.UserRegisterBody;
import com.huashengke.com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yangc on 2018/4/26.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/register")
    public Result<?> register(@RequestBody UserRegisterBody body){

        try {
            return Result.result(userService.register(body));
        } catch (UserException e) {

            return Result.error(e.getRc(), e.getMessage());
        }

    }


}
