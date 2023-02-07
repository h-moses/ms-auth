package com.ms.system.exception;

import com.ms.common.result.Result;
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

}
