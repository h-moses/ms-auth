package com.ms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ms.common.result.Result;
import com.ms.log.service.OperationLogService;
import com.ms.model.system.SysOperLog;
import com.ms.model.vo.SysOperLogQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "操作日志接口")
@Slf4j
@RestController
@RequestMapping("/admin/system/operLog")
public class SysOperLogController {

    @Resource
    private OperationLogService operationLogService;

    @ApiOperation("操作日志分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit,
                        SysOperLogQueryVo operLogQueryVo) {
        Page<SysOperLog> sysLoginLogPage = new Page<>(page, limit);
        LambdaQueryWrapper<SysOperLog> wrapper = Wrappers.lambdaQuery(SysOperLog.class).like(StringUtils.hasText(operLogQueryVo.getOperName()), SysOperLog::getOperName, operLogQueryVo.getOperName());
        Page<SysOperLog> page1 = operationLogService.page(sysLoginLogPage, wrapper);
        return Result.ok(page1);
    }
}
