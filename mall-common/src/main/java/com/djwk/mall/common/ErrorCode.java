package com.djwk.mall.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务错误码。
 * 编码规则（练手约定）：
 *   0          成功
 *   1xxx       通用错误（参数、鉴权、限流等）
 *   2xxx       用户域
 *   3xxx       商品/库存域
 *   4xxx       订单域
 *   5xxx       外部依赖（Feign 调用、数据库、Redis 等）
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, "成功"),

    // ===== 通用 1xxx =====
    PARAM_ERROR(1001, "参数错误"),
    UNAUTHORIZED(1002, "未认证或凭证无效"),
    FORBIDDEN(1003, "无权限访问"),
    NOT_FOUND(1004, "资源不存在"),
    RATE_LIMITED(1005, "请求过于频繁，请稍后再试"),
    IDEMPOTENT_CONFLICT(1006, "重复请求，幂等冲突"),

    // ===== 用户域 2xxx =====
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_PASSWORD_ERROR(2002, "用户名或密码错误"),
    USER_NAME_EXISTS(2003, "用户名已存在"),

    // ===== 商品/库存域 3xxx =====
    PRODUCT_NOT_FOUND(3001, "商品不存在"),
    STOCK_NOT_ENOUGH(3002, "库存不足"),
    STOCK_LOCK_FAILED(3003, "库存锁定失败"),

    // ===== 订单域 4xxx =====
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态不允许该操作"),
    ORDER_CREATE_FAILED(4003, "订单创建失败"),

    // ===== 外部依赖 5xxx =====
    FEIGN_CALL_FAILED(5001, "远程服务调用失败"),
    DB_ERROR(5002, "数据库操作失败"),
    CACHE_ERROR(5003, "缓存操作失败"),
    SEATA_TX_FAILED(5004, "分布式事务失败"),
    UNKNOWN_ERROR(9999, "系统繁忙，请稍后再试");

    private final int code;
    private final String message;
}
