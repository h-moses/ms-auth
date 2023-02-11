package com.ms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ms.common.result.Result;
import com.ms.common.utils.MD5Util;
import com.ms.model.system.SysRole;
import com.ms.model.system.SysUser;
import com.ms.model.vo.SysUserQueryVo;
import com.ms.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@Api(tags = "用户接口")
@RequestMapping("/admin/system/user")
public class SysUserController {

    @Resource
    private SysUserService service;

    @ApiOperation("用户列表")
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable("page") Long page, @PathVariable("limit") Long limit, SysUserQueryVo sysUserQueryVo) {
        Page<SysUser> page1 = new Page<>(page, limit);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>().like(StringUtils.hasText(sysUserQueryVo.getKeyword()), "username", sysUserQueryVo.getKeyword())
                .eq("is_deleted", 0)
                .orderByAsc("id");
        Page<SysUser> userPage = service.page(page1, wrapper);
        return Result.ok(userPage);
    }

    @ApiOperation("添加用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser sysUser) {
        String encrypt = MD5Util.encrypt(sysUser.getPassword());
        sysUser.setPassword(encrypt);
        boolean save = service.save(sysUser);
        if (save) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("根据ID查询")
    @GetMapping("getUser/{id}")
    public Result getUser(@PathVariable("id") String id) {
        SysUser byId = service.getById(id);
        return Result.ok(byId);
    }

    @ApiOperation("修改用户")
    @PostMapping("update")
    public Result update(@RequestBody SysUser sysUser) {
        boolean b = service.updateById(sysUser);
        if (b) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("remove/{id}")
    public Result delete(@PathVariable("id") String id) {
        boolean b = service.removeById(id);
        if (b) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("更改状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        SysUser byId = service.getById(id);
        byId.setStatus(status);
        boolean b = service.updateById(byId);
        if (b) {
            return Result.ok();
        }
        return Result.fail();
    }
}
