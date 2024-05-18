package com.unnamed.transformLink.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import static com.unnamed.transformLink.admin.comoon.constant.RedisLuaConstant.*;

/**
 * redis lua 脚本配置
 */
@Configuration
public class RedisLuaScriptConfiguration {

    @Bean("userLoginRedisScript")
    public DefaultRedisScript<Boolean> loginUserScript(){
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LOGIN_USER)));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean("userLoginOutRedisScript")
    public DefaultRedisScript<Boolean> loginOutUserScript(){
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LOGIN_OUT_USER)));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean("checkUserInfoRedisScript")
    public DefaultRedisScript<Boolean> getUserInfoScript(){
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(CHECK_USER_INFO)));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }
}
