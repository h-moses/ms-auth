package com.ms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ms.model.system.SysUser;
import com.ms.model.vo.RouterVo;
import com.ms.system.mapper.SysUserMapper;
import com.ms.system.service.SysMenuService;
import com.ms.system.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysMenuService menuService;

    @Override
    public Map<String, Object> getUserInfo(String username) {
        SysUser user = getOne(new QueryWrapper<SysUser>().eq("username", username));
        List<RouterVo> menuList = menuService.getMenuList(user.getId());
        List<String> buttonList = menuService.getButtonList(user.getId());

        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("roles", "[\"admin\"]");
        map.put("routers", menuList);
        map.put("buttons", buttonList );
        return map;
    }
}
