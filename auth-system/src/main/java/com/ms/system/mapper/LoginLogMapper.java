package com.ms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ms.model.system.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginLogMapper extends BaseMapper<SysLoginLog> {
}
