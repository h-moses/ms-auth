package com.ms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ms.common.result.Result;
import com.ms.model.system.SysRole;
import com.ms.model.vo.SysRoleQueryVo;
import com.ms.system.exception.CustomException;
import com.ms.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "角色接口")
@RequestMapping(value = "/admin/system/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @ApiOperation("查询所有角色")
    @GetMapping("findAll")
    public Result<List<SysRole>> roleList() {
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            throw new CustomException(500, "服务器不能访问");
        }
        return Result.ok(sysRoleService.list());
    }

    @ApiOperation("根据角色Id逻辑删除角色")
    @DeleteMapping("remove/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (sysRoleService.removeById(id)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result findRolePage(@PathVariable("page") Long page,
                               @PathVariable("limit") Long limit,
                               SysRoleQueryVo sysRoleQueryVo) {
        Page<SysRole> rolePage = new Page<>(page, limit);
        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>().like(StringUtils.hasText(sysRoleQueryVo.getRoleName()), "role_name", sysRoleQueryVo.getRoleName())
                .eq("is_deleted", 0)
                .orderByAsc("id");
        Page<SysRole> sysRolePage = sysRoleService.page(rolePage, wrapper);
        return Result.ok(sysRolePage);
    }

    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result saveRole(@RequestBody SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("根据id查询角色")
    @PostMapping("findRoleById/{id}")
    public Result findRoleById(@PathVariable("id") Long id) {
        SysRole byId = sysRoleService.getById(id);
        return Result.ok(byId);
    }

    @ApiOperation("修改角色")
    @PostMapping("update")
    public Result update(SysRole sysRole) {
        boolean b = sysRoleService.updateById(sysRole);
        if (b) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("批量删除角色")
    @DeleteMapping("batchDelete")
    public Result removeBatch(@RequestBody List<Long> ids) {
        boolean b = sysRoleService.removeBatchByIds(ids);
        if (b) {
            return Result.ok();
        }
        return Result.fail();
    }

}
