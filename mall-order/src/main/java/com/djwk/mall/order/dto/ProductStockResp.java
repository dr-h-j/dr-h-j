package com.djwk.mall.order.dto;

import lombok.Data;

/**
 * 扣减库存响应（Feign 调用返回，字段与 mall-product 的 DeductStockResp 对齐）。
 */
@Data
public class ProductStockResp {

    /** 扣减后剩余可用库存 */
    private Integer remainStock;
}
