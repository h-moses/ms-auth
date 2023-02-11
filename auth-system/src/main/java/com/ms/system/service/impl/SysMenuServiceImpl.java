package com.ms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ms.model.system.SysMenu;
import com.ms.model.system.SysRoleMenu;
import com.ms.model.system.SysUserRole;
import com.ms.model.vo.AssignMenuVo;
import com.ms.model.vo.RouterVo;
import com.ms.system.mapper.SysMenuMapper;
import com.ms.system.mapper.SysRoleMenuMapper;
import com.ms.system.mapper.SysUserRoleMapper;
import com.ms.system.service.SysMenuService;
import com.ms.system.utils.MenuHelper;
import com.ms.system.utils.RoutHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysMenuMapper menuMapper;

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

    @Override
    public List<RouterVo> getMenuList(String id) {
        List<SysMenu> menuList = new ArrayList<>();
        if ("1".equals(id)) {
            menuList = list(new QueryWrapper<SysMenu>().eq("status", 1).orderByDesc("sort_value"));
        } else {
            menuList = menuMapper.findMenuListByUserId(id);
        }

        List<SysMenu> sysMenus = MenuHelper.buildTree(menuList);
        List<RouterVo> routers = RoutHelper.buildRouters(sysMenus);
        return routers;
    }

//    private List<SysMenu> findMenuListByUserId(String id) {
//        LambdaQueryWrapper<SysUserRole> wrapper = Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, id).eq(SysUserRole::getIsDeleted, 0);
//        String roleId = Optional.ofNullable(userRoleMapper.selectOne(wrapper)).map(SysUserRole::getRoleId).orElse(null);
//        Optional.ofNullable(roleId).ifPresent(this::findRoleMenu);
//        return null;
//    }
//
//    private List<SysMenu> findRoleMenu(String s) {
//        LambdaQueryWrapper<SysRoleMenu> wrapper = Wrappers.lambdaQuery(SysRoleMenu.class).eq(SysRoleMenu::getRoleId, s).eq(SysRoleMenu::getIsDeleted, 0);
//        List<String> list = Optional.ofNullable(roleMenuMapper.selectList(wrapper)).get().stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
//        Optional.ofNullable(list).ifPresent(this::findMenuInfo);
//        return null;
//    }
//
//    private List<SysMenu> findMenuInfo(List<String> strings) {
//        List<SysMenu> list = new ArrayList<>();
//        strings.stream().forEach(consumer -> {
//            SysMenu sysMenu = menuMapper.selectOne(new QueryWrapper<SysMenu>().eq("id", consumer).eq("status", 1).eq("is_deleted", 0));
//            list.add(sysMenu);
//        });
//        return list;
//    }

    @Override
    public List<String> getButtonList(String id) {
        List<SysMenu> menuList;
        if ("1".equals(id)) {
            menuList = list(new QueryWrapper<SysMenu>().eq("status", 1).orderByDesc("sort_value"));
        } else {
            menuList = menuMapper.findMenuListByUserId(id);
        }
        List<String> collect = menuList.stream().filter(c -> c.getType() == 2).map(SysMenu::getPerms).collect(Collectors.toList());
        return collect;
    }
}
