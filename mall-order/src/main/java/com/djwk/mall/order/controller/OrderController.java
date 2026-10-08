package com.djwk.mall.order.controller;

import com.djwk.mall.common.Result;
import com.djwk.mall.order.dto.CreateOrderReq;
import com.djwk.mall.order.dto.CreateOrderResp;
import com.djwk.mall.order.entity.Order;
import com.djwk.mall.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单服务接口。网关路由：/order/**
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** 下单（演示跨服务调用：扣库存 + 落订单） */
    @PostMapping("/create")
    public Result<CreateOrderResp> create(@Valid @RequestBody CreateOrderReq req) {
        return Result.ok(orderService.createOrder(req));
    }

    /** 订单详情 */
    @GetMapping("/{id}")
    public Result<Order> detail(@PathVariable("id") Long id) {
        return Result.ok(orderService.getOrder(id));
    }
}
