package com.djwk.mall.order.controller;

import com.djwk.mall.common.Result;
import com.djwk.mall.order.dto.CreateOrderReq;
import com.djwk.mall.order.dto.CreateOrderResp;
import com.djwk.mall.order.entity.Order;
import com.djwk.mall.order.entity.OrderItem;
import com.djwk.mall.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /** 订单列表：userId 为空返回全部（商家后台），非空返回该用户订单（我的订单） */
    @GetMapping("/list")
    public Result<List<Order>> list(@RequestParam(value = "userId", required = false) Long userId) {
        return Result.ok(orderService.listOrders(userId));
    }

    /** 订单明细（一个订单可能多件商品） */
    @GetMapping("/{id}/items")
    public Result<List<OrderItem>> items(@PathVariable("id") Long id) {
        return Result.ok(orderService.getItems(id));
    }
}
