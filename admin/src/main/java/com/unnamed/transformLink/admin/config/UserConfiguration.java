package com.unnamed.transformLink.admin.config;

import com.unnamed.transformLink.admin.comoon.biz.user.UserTransmitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * 用户配置自动装配
 */
@Configuration
public class UserConfiguration {

    @Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter(StringRedisTemplate stringRedisTemplate, DefaultRedisScript<Boolean> checkUserInfoRedisScript) {
        FilterRegistrationBean<UserTransmitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserTransmitFilter(stringRedisTemplate, checkUserInfoRedisScript));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0);
        registrationBean.addInitParameter("excludedUris", "/api/transform-link/v1/user/login");
        return registrationBean;
    }

}
