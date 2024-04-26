package com.unnamed.transformLink.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import static com.unnamed.transformLink.admin.comoon.constant.RedisLuaConstant.LOGIN_USER_PATH;

/**
 * redis lua 脚本配置
 */
@Configuration
public class RedisLuaScriptConfiguration {

    @Bean("userLoginRedisScript")
    public DefaultRedisScript<Boolean> loginUserScript(){
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LOGIN_USER_PATH)));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }
}
