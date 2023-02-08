package com.ms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ms.common.result.Result;
import com.ms.model.system.SysRole;
import com.ms.model.system.SysUserRole;
import com.ms.model.vo.AssginRoleVo;
import com.ms.system.service.SysRoleService;
import com.ms.system.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(tags = "用户角色接口")
@RequestMapping("/admin/system/sysRole")
public class SysUserRoleController {

    @Resource
    private SysUserRoleService service;

    @Resource
    private SysRoleService sysRoleService;

    @ApiOperation("获取用户角色数据")
    @GetMapping("/assigned/{userId}")
    public Result assigned(@PathVariable("userId") String id) {
        List<SysRole> roleList = sysRoleService.list();
        List<SysUserRole> list = service.list(new QueryWrapper<SysUserRole>().eq("user_id", id).select("role_id"));
        HashMap<String, List> map = new HashMap<>();
        map.put("roles", roleList);
        map.put("assign", list.stream().map(SysUserRole::getRoleId).collect(Collectors.toList()));
        return Result.ok(map);
    }

    @ApiOperation("分配角色")
    @PostMapping("assign")
    public Result assign(@RequestBody AssginRoleVo assginRoleVo) {
        String userId = assginRoleVo.getUserId();
        service.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        for (String role_id:
             assginRoleVo.getRoleIdList()) {
            service.save(new SysUserRole(role_id, userId));
        }
        return Result.ok();
    }
}
