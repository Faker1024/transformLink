package com.unnamed.transformLink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unnamed.transformLink.admin.dao.entity.UserDO;
import com.unnamed.transformLink.admin.dto.req.UserRegisterReqDTO;
import com.unnamed.transformLink.admin.dto.req.UserUpdateReqDTO;
import com.unnamed.transformLink.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {
    /**
     * 根据用户查询用户信息
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 根据用户名查询是否存在
     * @param username 用户名
     * @return 存在返回True，不存在返回False
     */
    Boolean hasUserName(String username);

    /**
     * 注册用户
     * @param requestParam 注册用户请求参数
     */
    void Register(UserRegisterReqDTO requestParam);

    /**
     * 根据用户名更新用户信息
     * @param requestParam 更新用户请求参数
     */
    void Update(UserUpdateReqDTO requestParam);
}
