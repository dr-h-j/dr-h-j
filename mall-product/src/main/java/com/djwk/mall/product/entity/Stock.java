package com.djwk.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存实体。一个 SKU 一条记录。
 */
@Data
@TableName("t_stock")
public class Stock {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品 ID */
    private Long productId;

    /** SKU 编码（练手阶段直接用商品 ID 当 SKU） */
    private String skuCode;

    /** 可用库存 */
    private Integer stock;

    /** 冻结库存（预留字段，TODO 下单预占库存时用） */
    private Integer frozenStock;

    private LocalDateTime updateTime;
}
