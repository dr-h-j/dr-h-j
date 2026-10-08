package com.djwk.mall.product.dto;

import lombok.Data;

/**
 * 扣减库存响应。
 */
@Data
public class DeductStockResp {

    /** 扣减后剩余可用库存 */
    private Integer remainStock;

    public DeductStockResp(Integer remainStock) {
        this.remainStock = remainStock;
    }
}
