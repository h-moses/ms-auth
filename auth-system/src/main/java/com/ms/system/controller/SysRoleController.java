package com.ms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ms.common.result.Result;
import com.ms.log.annotation.Log;
import com.ms.log.enums.BusinessEnum;
import com.ms.model.system.SysRole;
import com.ms.model.system.SysUserRole;
import com.ms.model.vo.AssginRoleVo;
import com.ms.model.vo.SysRoleQueryVo;
import com.ms.system.service.SysRoleService;
import com.ms.system.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(tags = "角色接口")
@RequestMapping(value = "/admin/system/sysRole/")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserRoleService userRoleService;

//    @Log(title = "查询角色", business = BusinessEnum.OTHER)
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("查询所有角色")
    @GetMapping("findAll")
    public Result<List<SysRole>> roleList() {
        return Result.ok(sysRoleService.list());
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据角色Id逻辑删除角色")
    @DeleteMapping("remove/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (sysRoleService.removeById(id)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result findRolePage(@PathVariable("page") Long page,
                               @PathVariable("limit") Long limit,
                               SysRoleQueryVo sysRoleQueryVo) {
        Page<SysRole> rolePage = new Page<>(page, limit);
        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>().like(StringUtils.hasText(sysRoleQueryVo.getRoleName()), "role_name", sysRoleQueryVo.getRoleName())
                .orderByAsc("id");
        Page<SysRole> sysRolePage = sysRoleService.page(rolePage, wrapper);
        return Result.ok(sysRolePage);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result saveRole(@RequestBody SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return Result.ok();
        }
        return Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据id查询角色")
    @PostMapping("findRoleById/{id}")
    public Result findRoleById(@PathVariable("id") Long id) {
        SysRole byId = sysRoleService.getById(id);
        return Result.ok(byId);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改角色")
    @PostMapping("update")
    public Result update(@RequestBody SysRole sysRole) {
        boolean b = sysRoleService.updateById(sysRole);
        if (b) {
            return Result.ok();
        }
        return Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除角色")
    @DeleteMapping("batchDelete")
    public Result removeBatch(@RequestBody List<Long> ids) {
        boolean b = sysRoleService.removeBatchByIds(ids);
        if (b) {
            return Result.ok();
        }
        return Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("获取用户角色数据")
    @GetMapping("toAssign/{userId}")
    public Result assigned(@PathVariable("userId") String id) {
        List<SysRole> roleList = sysRoleService.list();
        List<SysUserRole> list = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", id));
        HashMap<String, List> map = new HashMap<>();
        map.put("allRoles", roleList);
        map.put("userRoleIds", list.stream().map(SysUserRole::getRoleId).collect(Collectors.toList()));
        return Result.ok(map);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("分配角色")
    @PostMapping("doAssign")
    public Result assign(@RequestBody AssginRoleVo assginRoleVo) {
        String userId = assginRoleVo.getUserId();
        userRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        for (String role_id:
                assginRoleVo.getRoleIdList()) {
            userRoleService.save(new SysUserRole(role_id, userId));
        }
        return Result.ok();
    }
}
