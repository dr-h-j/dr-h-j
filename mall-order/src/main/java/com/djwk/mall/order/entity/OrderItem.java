package com.djwk.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细表（一个订单多件商品）。
 */
@Data
@TableName("t_order_item")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单 ID */
    private Long orderId;

    /** 商品 ID */
    private Long productId;

    /** 商品名称（下单时冗余快照） */
    private String productName;

    /** 商品单价（快照） */
    private BigDecimal price;

    /** 购买数量 */
    private Integer count;
}
