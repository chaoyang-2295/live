package com.huashengke.com.user.controller;

import com.huashengke.com.tools.Result;
import com.huashengke.com.tools.exception.user.UserException;
import com.huashengke.com.user.body.UserRegisterBody;
import com.huashengke.com.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yangc on 2018/4/26.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @ApiOperation("通过手机号或者邮箱注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result<?> register(HttpServletRequest request,
                              @RequestBody UserRegisterBody body){

        try {
            String ip = request.getRemoteAddr();
            return Result.result(userService.register(ip, body));
        } catch (UserException e) {

            return Result.error(e.getRc(), e.getMessage());
        }
    }


    @ResponseBody
    @ApiOperation("通过手机号或者邮箱登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Result<?> login(@RequestParam("username") String username,
                           @RequestParam("password") String password){

        try {
            return Result.result(userService.login( username, password ));
        } catch (UserException e) {

            return Result.error(e.getRc(), e.getMessage());
        }
    }


    @ResponseBody
    @ApiOperation("新版绑定QQ号")
    @RequestMapping(value = "/bind/qq", method = RequestMethod.POST)
    public Result<?> newBindWithQQ(
            @ApiParam("用户ID")@RequestParam("userId")String userId,
            @ApiParam("openid")@RequestParam(value = "openid")String openid,
            @ApiParam("token")@RequestParam(value = "token")String token) {
        try {

            return Result.result(userService.bindQQ(userId, openid, token));
        } catch (UserException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    @ResponseBody
    @ApiOperation("新版绑定微信")
    @RequestMapping(value = "bind/weChat", method = RequestMethod.POST)
    public Result<?> newBindWithWeChat(
            @ApiParam("用户ID")@RequestParam("userId")String userId,
            @ApiParam("openid")@RequestParam(value = "openid")String openid,
            @ApiParam("token")@RequestParam(value = "token")String token) {
        try {


            return Result.result(userService.bindWeChat(userId, openid, token));
        } catch (UserException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }


    @ApiOperation("绑定手机号")
    @RequestMapping(value = "/bind/telephone", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> bindWithTelephone(
            @ApiParam("用户长id")@RequestParam("userId")String userId,
            @ApiParam("用户绑定手机号")@RequestParam("telephone")String telephone,
            @ApiParam("验证码")@RequestParam("code")String code) {
        try {
            return Result.result(userService.bindTelephone(userId, telephone, code));
        } catch (UserException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }


    @ApiOperation( value = "新版QQ注册接口")
    @RequestMapping(value = "/new/register/qq", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> registerWithQQ(@ApiParam("openid")@RequestParam(value = "openid")String openid,
                                    @ApiParam("token")@RequestParam(value = "token")String token) {
        try {

            return Result.result(userService.registWithQQ(openid, token));
        } catch (UserException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    @ApiOperation( value = "新微信注册接口")
    @RequestMapping(value = "/register/weChat", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> registerWithWeChat(@ApiParam("openid")@RequestParam(value = "openid")String openid,
                                        @ApiParam("token")@RequestParam(value = "token")String token) {
        try {

            return Result.result(userService.registWithWeChat(openid, token));
        } catch (UserException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }

    @RequestMapping(value = "/login/weChat")
    @ResponseBody
    public Result<?> wechatAuthenticate(@RequestParam("token") String token,
                                        @RequestParam("openid") String openid) {
        try {

            return Result.result(userService.loginWithWeChat(token, openid));
        } catch (UserException e) {
            return Result.error(e.getRc(), e.getMessage());
        }
    }


    @RequestMapping(value = "/login/qq")
    @ResponseBody
    public Result<?> qqAuthenticate(@RequestParam("token") String token,
                                    @RequestParam("openid") String openid) {
        try {

            return Result.result(userService.loginWithQQ(token, openid));
        } catch (UserException e) {
            return Result.error(e.getRc(), e);
        }
    }


}
