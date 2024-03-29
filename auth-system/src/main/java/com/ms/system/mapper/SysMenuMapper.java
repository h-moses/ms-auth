package com.ms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ms.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> findMenuListByUserId(@Param("userId") String id);
}
