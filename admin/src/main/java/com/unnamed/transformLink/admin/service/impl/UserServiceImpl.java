package com.unnamed.transformLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unnamed.transformLink.admin.comoon.convention.exception.ClientException;
import com.unnamed.transformLink.admin.dao.entity.UserDO;
import com.unnamed.transformLink.admin.dao.mapper.UserMapper;
import com.unnamed.transformLink.admin.dto.req.UserRegisterReqDTO;
import com.unnamed.transformLink.admin.dto.resp.UserRespDTO;
import com.unnamed.transformLink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.unnamed.transformLink.admin.comoon.enums.UserErrorCodeEnum.USER_EXIST;
import static com.unnamed.transformLink.admin.comoon.enums.UserErrorCodeEnum.USER_Name_EXIST;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUserName(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void Register(UserRegisterReqDTO requestParam) {
        if (hasUserName(requestParam.getUsername())) {
            throw  new ClientException(USER_Name_EXIST);
        }
        int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
        if (inserted < 1) {
            throw new ClientException(USER_EXIST);
        }
        userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
    }


}
