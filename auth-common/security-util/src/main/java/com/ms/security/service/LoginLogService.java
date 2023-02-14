package com.ms.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ms.model.system.SysLoginLog;

public interface LoginLogService extends IService<SysLoginLog> {

    public void recordLoginLog(String username, Integer status, String ip, String message);
}
