package com.ms.system.exception;

import com.ms.common.result.Result;
import com.ms.common.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        return Result.fail().message("执行全局异常处理");
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e) {
        return Result.fail().message("执行特定异常处理");
    }

    @ExceptionHandler(CustomException.class)
    public Result error(CustomException e) {
        return Result.fail().message(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result error(AccessDeniedException e) {
        return Result.fail().code(ResultCodeEnum.PERMISSION.getCode()).message("没有当前功能的操作权限");
    }

}
