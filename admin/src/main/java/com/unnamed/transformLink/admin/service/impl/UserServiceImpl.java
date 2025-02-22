package com.unnamed.transformLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unnamed.transformLink.admin.comoon.biz.user.UserContext;
import com.unnamed.transformLink.admin.comoon.convention.exception.ClientException;
import com.unnamed.transformLink.admin.dao.entity.UserDO;
import com.unnamed.transformLink.admin.dao.mapper.UserMapper;
import com.unnamed.transformLink.admin.dto.req.*;
import com.unnamed.transformLink.admin.dto.resp.UserLoginRespDTO;
import com.unnamed.transformLink.admin.dto.resp.UserRespDTO;
import com.unnamed.transformLink.admin.service.GroupService;
import com.unnamed.transformLink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static com.unnamed.transformLink.admin.comoon.constant.RedisCacheConstant.Lock_USER_REGISTER_KEY;
import static com.unnamed.transformLink.admin.comoon.constant.RedisCacheConstant.Login_USER_KEY;
import static com.unnamed.transformLink.admin.comoon.convention.errorcode.BaseErrorCode.CLIENT_ERROR;
import static com.unnamed.transformLink.admin.comoon.enums.UserErrorCodeEnum.*;

/**
 * 用户接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Boolean> userLoginRedisScript;
    private final DefaultRedisScript<Boolean> userLoginOutRedisScript;
    private final GroupService groupService;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        return BeanUtil.toBean(userDO, UserRespDTO.class);
    }

    @Override
    public Boolean hasUserName(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if (hasUserName(requestParam.getUsername())) {
            throw  new ClientException(USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(Strings.concat(Lock_USER_REGISTER_KEY, requestParam.getUsername()));
        try{
            if (!lock.tryLock()) {
                throw new ClientException(USER_EXIST);
            }
            int inserted;
            try{
                inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
            }catch (Exception e){
                throw new ClientException(USER_EXIST);
            }
            if (inserted < 1) {
                throw new ClientException(USER_SAVE_ERROR);
            }
            userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
            groupService.saveGroup(requestParam.getUsername(),"默认分组");

        }finally {
            lock.unlock();
        }
    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        String userId = UserContext.getUserId();
        if (Strings.isBlank(userId) || !userId.equals(requestParam.getUsername())){ throw new ClientException(USER_NULL);}
        String json = stringRedisTemplate.opsForValue().get(requestParam.getToken());
        UserDO userDO = JSON.parseObject(json, UserDO.class);
        assert userDO != null;
        if (!Objects.equals(userDO.getUsername(), requestParam.getUsername())) {
            throw new ClientException(CLIENT_ERROR);
        }
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), queryWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // 查询用户是否存在
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException(USER_NULL);
        }
        /**
         * 多设备登录，三台
         * key: Login_$username
         * value:
         *      key:token
         *      value:userInfo
         */
        String loginKey = Login_USER_KEY + requestParam.getUsername();
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.execute(userLoginRedisScript, null, loginKey, uuid, String.valueOf(System.currentTimeMillis()/1000), JSONUtil.parse(userDO).toString());
        return new UserLoginRespDTO(uuid);
    }



    @Override
    public Boolean checkLogin(UserCheckLoginReqDTO requestParam) {
        return stringRedisTemplate.opsForHash().hasKey(Login_USER_KEY + requestParam.getUsername(), requestParam.getToken());
    }

    @Override
    public void logout(UserLogoutReqDTO requestParam) {
        stringRedisTemplate.execute(userLoginOutRedisScript, null, Login_USER_KEY+requestParam.getUsername(), requestParam.getToken());
    }


}
