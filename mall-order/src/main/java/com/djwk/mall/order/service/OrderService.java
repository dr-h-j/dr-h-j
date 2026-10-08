package com.djwk.mall.order.service;

import com.djwk.mall.order.dto.CreateOrderReq;
import com.djwk.mall.order.dto.CreateOrderResp;
import com.djwk.mall.order.entity.Order;
import com.djwk.mall.order.entity.OrderItem;

import java.util.List;

/**
 * 订单服务接口。
 */
public interface OrderService {

    /** 下单：Feign 扣库存 + 本地落订单 */
    CreateOrderResp createOrder(CreateOrderReq req);

    /** 订单详情 */
    Order getOrder(Long id);

    /** 订单列表：userId 为空时返回全部（商家后台），非空时返回该用户订单（我的订单） */
    List<Order> listOrders(Long userId);

    /** 订单明细（一个订单可能多件商品） */
    List<OrderItem> getItems(Long orderId);
}
