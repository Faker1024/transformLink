package com.unnamed.transformLink.admin.comoon.biz.user;

import cn.hutool.json.JSONUtil;
import com.unnamed.transformLink.admin.comoon.convention.exception.ClientException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.io.IOException;

import static com.unnamed.transformLink.admin.comoon.constant.RedisCacheConstant.Login_USER_KEY;
import static com.unnamed.transformLink.admin.comoon.constant.TransformLinkConstant.*;
import static com.unnamed.transformLink.admin.comoon.enums.UserErrorCodeEnum.USER_NULL;

/**
 * 用户信息传输过滤器
 */
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;

    private final DefaultRedisScript<Boolean> checkUserInfoRedisScript;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = httpServletRequest.getHeader(USERNAME);
        String token = httpServletRequest.getHeader(TOKEN);
        // 获得的不是用户信息，是时间戳
        if (!StringUtils.isBlank(username) && !StringUtils.isBlank(token)){
            String hashKey = Login_USER_KEY + username;
            Boolean flag = stringRedisTemplate.execute(checkUserInfoRedisScript, null, hashKey, token);
            if (Boolean.TRUE.equals(flag)){
                String userInfo = stringRedisTemplate.opsForValue().get(hashKey + INFO);
                if (StringUtils.isEmpty(userInfo)) {
                    throw new ClientException(USER_NULL);
                }
                UserInfoDTO userInfoDTO = JSONUtil.toBean(userInfo, UserInfoDTO.class);
                UserContext.setUser(userInfoDTO);
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}