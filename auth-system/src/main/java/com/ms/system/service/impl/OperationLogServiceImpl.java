package com.ms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ms.log.service.OperationLogService;
import com.ms.model.system.SysOperLog;
import com.ms.system.mapper.OperationLogMapper;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, SysOperLog> implements OperationLogService {
}
