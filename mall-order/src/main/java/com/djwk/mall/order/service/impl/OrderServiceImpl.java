package com.djwk.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.djwk.mall.common.BizException;
import com.djwk.mall.common.ErrorCode;
import com.djwk.mall.common.Result;
import com.djwk.mall.order.dto.CreateOrderReq;
import com.djwk.mall.order.dto.CreateOrderResp;
import com.djwk.mall.order.dto.DeductStockReq;
import com.djwk.mall.order.dto.ProductInfo;
import com.djwk.mall.order.dto.ProductStockResp;
import com.djwk.mall.order.entity.Order;
import com.djwk.mall.order.entity.OrderItem;
import com.djwk.mall.order.feign.ProductClient;
import com.djwk.mall.order.mapper.OrderItemMapper;
import com.djwk.mall.order.mapper.OrderMapper;
import com.djwk.mall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现 —— 本项目的核心教学链路。
 *
 * 下单流程：
 *   1. Feign 调 mall-product 查商品信息（拿名称/单价快照）；
 *   2. Feign 调 mall-product 原子扣库存（防超卖）；
 *   3. 本地事务落订单主表 + 明细表。
 *
 * 分布式事务（进阶，默认关闭）：
 *   在 createOrder 上加 @GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
 *   并启动 Seata Server（见 README「进阶玩法」），即可让"扣库存 + 落订单"成为全局事务，
 *   任一步失败自动回滚，解决跨服务数据一致性。
 *
 * 幂等（进阶，TODO）：
 *   下单前用 Redis SETNX order:create:{userId}:{productId} 防重复提交。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductClient productClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    // @GlobalTransactional(name = "create-order", rollbackFor = Exception.class) // 开启 Seata 时启用
    public CreateOrderResp createOrder(CreateOrderReq req) {
        // 1. 查商品信息（快照）
        Result<ProductInfo> productResult = productClient.getProduct(req.getProductId());
        ProductInfo product = unwrap(productResult, "查询商品信息失败");
        if (product.getStatus() == null || product.getStatus() != 1) {
            throw new BizException(ErrorCode.PRODUCT_NOT_FOUND, "商品未上架");
        }

        // 2. 扣减库存（mall-product 内部原子 UPDATE，库存不足会抛 STOCK_NOT_ENOUGH）
        DeductStockReq deductReq = new DeductStockReq();
        deductReq.setProductId(req.getProductId());
        deductReq.setCount(req.getCount());
        Result<ProductStockResp> stockResult = productClient.deductStock(deductReq);
        ProductStockResp stockResp = unwrap(stockResult, "扣减库存失败");

        // 3. 本地落订单（主表 + 明细）
        String orderNo = generateOrderNo();
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(req.getUserId());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(req.getCount())));
        order.setStatus(1); // 待支付
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.insert(order);

        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setProductId(req.getProductId());
        item.setProductName(product.getName());
        item.setPrice(product.getPrice());
        item.setCount(req.getCount());
        orderItemMapper.insert(item);

        log.info("下单成功: orderId={}, orderNo={}, userId={}, productId={}, count={}",
                order.getId(), orderNo, req.getUserId(), req.getProductId(), req.getCount());

        return new CreateOrderResp(order.getId(), orderNo,
                order.getTotalAmount().toPlainString(), stockResp.getRemainStock());
    }

    @Override
    public Order getOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BizException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    public List<Order> listOrders(Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(wrapper);
    }

    @Override
    public List<OrderItem> getItems(Long orderId) {
        return orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
    }

    /** 解包 Result，业务码非 0 或远程异常时抛 BizException */
    private <T> T unwrap(Result<T> result, String failMsg) {
        if (result == null || result.getCode() != 0) {
            throw new BizException(ErrorCode.FEIGN_CALL_FAILED,
                    failMsg + (result == null ? "" : "：" + result.getMessage()));
        }
        return result.getData();
    }

    /** 练手版订单号：时间戳 + UUID 片段。TODO 换雪花算法（分布式 ID 教学点） */
    private String generateOrderNo() {
        return "M" + System.currentTimeMillis()
                + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
