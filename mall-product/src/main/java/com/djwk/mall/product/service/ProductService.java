package com.djwk.mall.product.service;

import com.djwk.mall.product.dto.DeductStockReq;
import com.djwk.mall.product.dto.DeductStockResp;
import com.djwk.mall.product.entity.Product;

import java.util.List;

/**
 * 商品/库存服务接口。
 */
public interface ProductService {

    /** 商品列表 */
    List<Product> listProducts();

    /** 商品详情 */
    Product getProduct(Long id);

    /** 扣减库存（原子 SQL，防超卖） */
    DeductStockResp deductStock(DeductStockReq req);

    /** 查询库存 */
    Integer getStock(Long productId);
}
