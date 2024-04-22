package com.unnamed.transformLink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.unnamed.transformLink.admin.comoon.convention.result.Result;
import com.unnamed.transformLink.admin.comoon.convention.result.Results;
import com.unnamed.transformLink.admin.dto.resp.UserActualRespDTO;
import com.unnamed.transformLink.admin.dto.resp.UserRespDTO;
import com.unnamed.transformLink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/api/transformlink/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 根据用户名查询信息
     */
    @GetMapping("/api/transformlink/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }
}
