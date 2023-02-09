package com.ms.system.controller;

import com.ms.common.result.Result;
import com.ms.model.system.SysMenu;
import com.ms.model.vo.AssignMenuVo;
import com.ms.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@Api("菜单接口")
@RequestMapping("/admin/system/menu")
public class SysMenuController {

    @Resource
    private SysMenuService menuService;

    @ApiOperation("菜单列表")
    @GetMapping("findNodes")
    public Result findNodes() {
        List<SysMenu> list = menuService.findNodes();
        return Result.ok(list);
    }


    @ApiOperation("添加菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu) {
        boolean save = menuService.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation("根据Id查询")
    @GetMapping("findNode/{id}")
    public Result findNode(@PathVariable("id") String id) {
        SysMenu byId = menuService.getById(id);
        return Result.ok(byId);
    }

    @ApiOperation("修改菜单")
    @PostMapping("update")
    public Result update(@RequestBody SysMenu sysMenu) {
        menuService.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id") String id) {
        try {
            menuService.removeMenuById(id);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @ApiOperation("根据角色获取菜单")
    @GetMapping("find/{roleId}")
    public Result find(@PathVariable("roleId") Long id) {
        List<SysMenu> menuByRoleId = menuService.findMenuByRoleId(id);
        return Result.ok(menuByRoleId);
    }

    @ApiOperation("给角色分配菜单")
    @PostMapping("assignMenu")
    public Result assignMenu(@RequestBody AssignMenuVo assignMenuVo) {
        menuService.assignMenu(assignMenuVo);
        return Result.ok();
    }
}
