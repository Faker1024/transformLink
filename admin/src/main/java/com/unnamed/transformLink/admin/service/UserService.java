package com.unnamed.transformLink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unnamed.transformLink.admin.dao.entity.UserDO;
import com.unnamed.transformLink.admin.dto.resp.UserActualRespDTO;
import com.unnamed.transformLink.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);

}
