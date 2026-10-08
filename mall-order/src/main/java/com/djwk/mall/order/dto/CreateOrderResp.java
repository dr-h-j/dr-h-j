package com.djwk.mall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下单响应。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResp {

    private Long orderId;

    private String orderNo;

    /** 订单总金额（元） */
    private String totalAmount;

    /** 下单后剩余库存 */
    private Integer remainStock;
}
