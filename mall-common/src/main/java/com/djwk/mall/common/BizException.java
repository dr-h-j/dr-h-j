package com.djwk.mall.common;

import lombok.Getter;

/**
 * 业务异常。业务代码中直接 throw 即可，由全局异常处理器统一转成 Result。
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }
}
