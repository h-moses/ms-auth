package com.ms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ms.common.result.Result;
import com.ms.common.utils.JwtUtil;
import com.ms.common.utils.MD5Util;
import com.ms.model.system.SysUser;
import com.ms.model.vo.LoginVo;
import com.ms.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "用户登录接口")
@Slf4j
@RequestMapping("/admin/system/index")
public class IndexController {

    @Resource
    private SysUserService service;

    @ApiOperation("用户登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {

        SysUser user = service.getOne(new QueryWrapper<SysUser>().eq("username", loginVo.getUsername()));

        if (null == user) {
            return Result.fail("用户不存在");
        }
        String password = loginVo.getPassword();
        String encrypt = MD5Util.encrypt(password);
        if (!user.getPassword().equals(encrypt)) {
            return Result.fail("密码不正确");
        }
        if (user.getStatus() == 0) {
            return Result.fail("用户被禁用");
        }

        String token = JwtUtil.createToken(user.getId(), user.getUsername());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.ok(map);
    }

    @ApiOperation("获取用户权限信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUsername(token);
        Map<String, Object> map = service.getUserInfo(username);
        return Result.ok(map);
    }
}
