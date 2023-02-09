package com.ms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ms.model.system.SysMenu;
import com.ms.model.vo.AssignMenuVo;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findNodes();

    void removeMenuById(String id) throws Exception;

    List<SysMenu> findMenuByRoleId(Long id);

    void assignMenu(AssignMenuVo assignMenuVo);
}
