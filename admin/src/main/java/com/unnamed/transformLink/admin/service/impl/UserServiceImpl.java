package com.unnamed.transformLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unnamed.transformLink.admin.comoon.convention.exception.ClientException;
import com.unnamed.transformLink.admin.dao.entity.UserDO;
import com.unnamed.transformLink.admin.dao.mapper.UserMapper;
import com.unnamed.transformLink.admin.dto.req.UserCheckLoginReqDTO;
import com.unnamed.transformLink.admin.dto.req.UserLoginReqDTO;
import com.unnamed.transformLink.admin.dto.req.UserRegisterReqDTO;
import com.unnamed.transformLink.admin.dto.req.UserUpdateReqDTO;
import com.unnamed.transformLink.admin.dto.resp.UserLoginRespDTO;
import com.unnamed.transformLink.admin.dto.resp.UserRespDTO;
import com.unnamed.transformLink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.unnamed.transformLink.admin.comoon.constant.RedisCacheConstant.Lock_USER_REGISTER_KEY;
import static com.unnamed.transformLink.admin.comoon.constant.RedisCacheConstant.Login_USER_KEY;
import static com.unnamed.transformLink.admin.comoon.convention.errorcode.BaseErrorCode.CLIENT_ERROR;
import static com.unnamed.transformLink.admin.comoon.enums.UserErrorCodeEnum.*;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Boolean> userLoginRedisScript;

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
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void Register(UserRegisterReqDTO requestParam) {
        if (hasUserName(requestParam.getUsername())) {
            throw  new ClientException(USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(Strings.concat(Lock_USER_REGISTER_KEY, requestParam.getUsername()));
        try{
            if (!lock.tryLock()) throw new ClientException(USER_EXIST);
            int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
            if (inserted < 1) {
                throw new ClientException(USER_EXIST);
            }
            userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void Update(UserUpdateReqDTO requestParam) {
//        TODO 验证用户名是否为当前登录用户
        String json = stringRedisTemplate.opsForValue().get(requestParam.getToken());
        UserDO userDO = JSON.parseObject(json, UserDO.class);
        assert userDO != null;
        if (userDO.getUsername() != requestParam.getUsername()) throw new ClientException(CLIENT_ERROR);
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), queryWrapper);
    }

    @Override
    public UserLoginRespDTO Login(UserLoginReqDTO requestParam) {
        // 查询用户是否存在
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) throw new ClientException(USER_NULL);
        /**
         * 多设备登录，三台
         * key: Login_$username
         * value:
         *      key:token
         *      value:userInfo
         */
        String loginKey = Login_USER_KEY + requestParam.getUsername();
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.execute(userLoginRedisScript, null, loginKey, uuid, String.valueOf(System.currentTimeMillis()));
        return new UserLoginRespDTO(uuid);
    }



    @Override
    public Boolean checkLogin(UserCheckLoginReqDTO requestParam) {
        return stringRedisTemplate.opsForHash().hasKey(Login_USER_KEY + requestParam.getUsername(), requestParam.getToken());
    }


}
