package com.djwk.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表。
 */
@Data
@TableName("t_order")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务订单号（幂等键，TODO 幂等场景使用） */
    private String orderNo;

    /** 下单用户 ID */
    private Long userId;

    /** 订单总金额（元） */
    private BigDecimal totalAmount;

    /** 状态：1 待支付 2 已支付 3 已发货 4 已完成 5 已取消 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
