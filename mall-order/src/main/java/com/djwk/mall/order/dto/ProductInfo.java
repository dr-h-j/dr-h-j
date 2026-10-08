package com.djwk.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品信息（Feign 调用返回，字段与 mall-product 的 Product 对齐）。
 */
@Data
public class ProductInfo {

    private Long id;

    private String name;

    private BigDecimal price;

    private Integer status;
}
