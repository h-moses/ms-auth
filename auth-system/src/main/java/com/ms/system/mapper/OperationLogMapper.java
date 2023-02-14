package com.ms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ms.model.system.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<SysOperLog> {
}
