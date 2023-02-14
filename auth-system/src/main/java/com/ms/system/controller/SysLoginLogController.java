package com.ms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ms.common.result.Result;
import com.ms.model.system.SysLoginLog;
import com.ms.model.vo.SysLoginLogQueryVo;
import com.ms.security.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "登录日志接口")
@Slf4j
@RestController
@RequestMapping("/admin/system/loginLog")
public class SysLoginLogController {

    @Resource
    private LoginLogService loginLogService;

    @ApiOperation("分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit,
                        SysLoginLogQueryVo loginLogQueryVo) {
        Page<SysLoginLog> sysLoginLogPage = new Page<>(page, limit);
        LambdaQueryWrapper<SysLoginLog> wrapper = Wrappers.lambdaQuery(SysLoginLog.class).like(StringUtils.hasText(loginLogQueryVo.getUsername()), SysLoginLog::getUsername, loginLogQueryVo.getUsername());
        Page<SysLoginLog> page1 = loginLogService.page(sysLoginLogPage, wrapper);
        return Result.ok(page1);
    }
}
