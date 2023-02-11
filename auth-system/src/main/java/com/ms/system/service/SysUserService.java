package com.ms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ms.model.system.SysUser;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    Map<String, Object> getUserInfo(String username);
}
