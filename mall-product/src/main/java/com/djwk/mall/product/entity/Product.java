package com.djwk.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体。
 */
@Data
@TableName("t_product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品名称 */
    private String name;

    /** 商品图片（占位） */
    private String image;

    /** 单价（元） */
    private BigDecimal price;

    /** 状态：1 上架 0 下架 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
