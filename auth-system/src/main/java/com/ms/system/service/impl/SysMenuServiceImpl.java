package com.ms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ms.model.system.SysMenu;
import com.ms.model.system.SysRoleMenu;
import com.ms.model.vo.AssignMenuVo;
import com.ms.system.mapper.SysMenuMapper;
import com.ms.system.mapper.SysRoleMenuMapper;
import com.ms.system.service.SysMenuService;
import com.ms.system.utils.MenuHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> list = list();
        List<SysMenu> sysMenus = MenuHelper.buildTree(list);
        return sysMenus;
    }

    @Override
    public void removeMenuById(String id) throws Exception {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<SysMenu>().eq("parent_id", id);
        long count = count(wrapper);
        if (count > 0) {
            throw new Exception("请先删除子菜单");
        } else {
            removeById(id);
        }
    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long id) {
        List<SysMenu> menuList = list(new QueryWrapper<SysMenu>().eq("status", 1));

        List<SysRoleMenu> roleMenuList = roleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<String> list = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        menuList.stream().forEach(consumer -> {
            if (list.contains(consumer.getId())) {
                consumer.setSelect(true);
            } else {
                consumer.setSelect(false);
            }
        });

        List<SysMenu> sysMenus = MenuHelper.buildTree(menuList);
        return sysMenus;
    }

    @Override
    public void assignMenu(AssignMenuVo assignMenuVo) {
        roleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("role_id", assignMenuVo.getRoleId()));
        List<String> menuIdList = assignMenuVo.getMenuIdList();
        for (String menuId:
             menuIdList) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenu.setMenuId(menuId);
            roleMenuMapper.insert(sysRoleMenu);
        }
    }
}
