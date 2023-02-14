package com.ms.security.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.result.Result;
import com.ms.common.result.ResultCodeEnum;
import com.ms.common.utils.IPUtil;
import com.ms.common.utils.JwtUtil;
import com.ms.common.utils.ResponseUtil;
import com.ms.model.vo.LoginVo;
import com.ms.security.custom.AuthUser;
import com.ms.security.service.LoginLogService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;

    private LoginLogService loginLogService;

    public LoginFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate, LoginLogService loginLogService) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
        this.loginLogService = loginLogService;
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        获取认证对象
        AuthUser user = (AuthUser) authResult.getPrincipal();
//        保存权限数据到Redis
        redisTemplate.opsForValue().set(user.getUsername(), JSON.toJSONString(user.getAuthorities()));
//        生成token
        String token = JwtUtil.createToken(user.getUser().getId(), user.getUser().getUsername());
//        记录登录日志
        loginLogService.recordLoginLog(user.getUsername(), 1, IPUtil.getIpAddress(request), "登录成功");
//        返回
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        if (failed.getCause() instanceof RuntimeException) {
            ResponseUtil.out(response, Result.build(null, 204, failed.getMessage()));
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
        }
    }
}
