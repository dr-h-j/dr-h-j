package com.djwk.mall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：把各种异常统一转成 Result，避免异常堆栈直接暴露给调用方。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 业务异常 */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBiz(BizException e) {
        log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /** @Valid 参数校验失败（@RequestBody） */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValid(MethodArgumentNotValidException e) {
        String msg = firstFieldError(e.getBindingResult().getFieldError());
        return Result.fail(ErrorCode.PARAM_ERROR, msg);
    }

    /** 参数绑定失败（表单） */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBind(BindException e) {
        String msg = firstFieldError(e.getBindingResult().getFieldError());
        return Result.fail(ErrorCode.PARAM_ERROR, msg);
    }

    /** 兜底异常 */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleOther(Exception e) {
        log.error("系统异常", e);
        return Result.fail(ErrorCode.UNKNOWN_ERROR);
    }

    private String firstFieldError(FieldError fieldError) {
        return fieldError == null
                ? ErrorCode.PARAM_ERROR.getMessage()
                : fieldError.getField() + " " + fieldError.getDefaultMessage();
    }
}
