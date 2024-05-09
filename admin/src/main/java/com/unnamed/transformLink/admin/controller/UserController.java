package com.unnamed.transformLink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.unnamed.transformLink.admin.comoon.convention.result.Result;
import com.unnamed.transformLink.admin.comoon.convention.result.Results;
import com.unnamed.transformLink.admin.dto.req.*;
import com.unnamed.transformLink.admin.dto.resp.UserActualRespDTO;
import com.unnamed.transformLink.admin.dto.resp.UserLoginRespDTO;
import com.unnamed.transformLink.admin.dto.resp.UserRespDTO;
import com.unnamed.transformLink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询信息
     */
    @GetMapping("/api/transform-link/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 根据用户名查询信息
     */
    @GetMapping("/api/transform-link/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }

    /**
     * 查询用户名是否存在
     */
    @GetMapping("/api/transform-link/v1/user/has-username")
    public Result<Boolean> hasUserName(@RequestParam("username") String username) {
        return Results.success(!userService.hasUserName(username));
    }

    /**
     * 注册用户
     */
    @PostMapping("/api/transform-link/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.Register(requestParam);
        return Results.success();
    }


    /**
     * 更新用户信息
     */
    @PutMapping("/api/transform-link/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam){
        userService.Update(requestParam);
        return Results.success();
    }

    /**
     * 登录用户
     */
    @PostMapping("/api/transform-link/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam){
        return Results.success(userService.Login(requestParam));
    }

    /**
     * 检查用户是否登录
     */
    @GetMapping("/api/transform-link/v1/user/check-login")
    public Result<Boolean> checkLogin(UserCheckLoginReqDTO requestParam){
        return Results.success(userService.checkLogin(requestParam));
    }

    /**
     * 注销用户
     */
    @DeleteMapping("/api/transform-link/v1/user")
    public Result<Void> logout(UserLogoutReqDTO requestParam){
        userService.logout(requestParam);
        return Results.success();
    }
}
