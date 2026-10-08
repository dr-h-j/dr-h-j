package com.djwk.mall.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 扣减库存请求（order 服务通过 Feign 调用）。
 */
@Data
public class DeductStockReq {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotNull(message = "扣减数量不能为空")
    @Min(value = 1, message = "扣减数量至少为 1")
    private Integer count;
}
