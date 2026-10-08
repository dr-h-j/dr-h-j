package com.djwk.mall.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应体。
 * 所有服务对外返回都走这个结构，网关 / Feign / 前端约定一致。
 *
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {

    /** 业务码，0 表示成功，非 0 见 ErrorCode */
    private int code;

    /** 提示信息 */
    private String message;

    /** 业务数据 */
    private T data;

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.code = ErrorCode.SUCCESS.getCode();
        r.message = ErrorCode.SUCCESS.getMessage();
        r.data = data;
        return r;
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return fail(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> Result<T> fail(ErrorCode errorCode, String message) {
        return fail(errorCode.getCode(), message);
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }
}
