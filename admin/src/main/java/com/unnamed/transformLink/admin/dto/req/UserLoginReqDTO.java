package com.unnamed.transformLink.admin.dto.req;

import lombok.Data;

/**
 * 用户登录接口请求参数
 */
@Data
public class UserLoginReqDTO {

    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
}
