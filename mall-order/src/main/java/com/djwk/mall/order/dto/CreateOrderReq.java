package com.djwk.mall.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 下单请求（练手阶段一单一件商品，方便演示）。
 */
@Data
public class CreateOrderReq {

    /** 下单用户 ID（TODO 网关鉴权后从 X-User-Id 头取，前端可不传） */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量至少为 1")
    private Integer count;
}
