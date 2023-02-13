package com.ms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ms.model.system.SysUser;
import com.ms.security.custom.AuthUser;
import com.ms.system.service.SysMenuService;
import com.ms.system.service.SysUserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private SysUserService userService;

    @Resource
    private SysMenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException {
        System.out.println("使用自定义的认证逻辑");
        SysUser sysUser = userService.getOne(new QueryWrapper<SysUser>().eq("username", username));
        if (null == sysUser) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (sysUser.getStatus() == 0) {
            throw new RuntimeException("用户被禁用");
        }
        List<String> buttonList = menuService.getButtonList(sysUser.getId());

//        转换成Security要求的格式
        List<SimpleGrantedAuthority> authorities = buttonList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new AuthUser(sysUser, authorities);
    }
}
