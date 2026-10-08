package com.djwk.mall.order.service;

import com.djwk.mall.order.dto.CreateOrderReq;
import com.djwk.mall.order.dto.CreateOrderResp;
import com.djwk.mall.order.entity.Order;

/**
 * 订单服务接口。
 */
public interface OrderService {

    /** 下单：Feign 扣库存 + 本地落订单 */
    CreateOrderResp createOrder(CreateOrderReq req);

    /** 订单详情 */
    Order getOrder(Long id);
}
