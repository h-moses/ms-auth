package com.ms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ms.model.system.SysLoginLog;
import com.ms.security.service.LoginLogService;
import com.ms.system.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, SysLoginLog> implements LoginLogService {
    @Override
    public void recordLoginLog(String username, Integer status, String ip, String message) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setStatus(status);
        sysLoginLog.setIpaddr(ip);
        sysLoginLog.setMsg(message);
        save(sysLoginLog);
    }
}

