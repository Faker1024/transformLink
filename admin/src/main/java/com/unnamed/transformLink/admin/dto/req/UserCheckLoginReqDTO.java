package com.unnamed.transformLink.admin.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCheckLoginReqDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录token
     */
    private String token;
}
