package com.djwk.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.djwk.mall.common.BizException;
import com.djwk.mall.common.ErrorCode;
import com.djwk.mall.product.dto.DeductStockReq;
import com.djwk.mall.product.dto.DeductStockResp;
import com.djwk.mall.product.entity.Product;
import com.djwk.mall.product.entity.Stock;
import com.djwk.mall.product.mapper.ProductMapper;
import com.djwk.mall.product.mapper.StockMapper;
import com.djwk.mall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品/库存服务实现。
 *
 * 核心教学点：扣减库存用一条带条件的原子 UPDATE
 *   UPDATE t_stock SET stock = stock - ? WHERE product_id = ? AND stock >= ?
 *   利用行锁 + 条件判断天然防超卖，不需要分布式锁。
 * 高并发进阶（TODO）：Redis Lua 预扣 + 异步落库、冻结库存、库存回补。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final StockMapper stockMapper;

    @Override
    public List<Product> listProducts() {
        return productMapper.selectList(
                new LambdaQueryWrapper<Product>().eq(Product::getStatus, 1));
    }

    @Override
    public Product getProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BizException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return product;
    }

    /**
     * 原子扣减库存：
     * update ... set stock = stock - #{count} where product_id = #{id} and stock >= #{count}
     * 影响行数为 0 表示库存不足。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeductStockResp deductStock(DeductStockReq req) {
        getProduct(req.getProductId()); // 不存在则抛异常

        int updated = stockMapper.update(null, new LambdaUpdateWrapper<Stock>()
                .setSql("stock = stock - {0}", req.getCount())
                .eq(Stock::getProductId, req.getProductId())
                .ge(Stock::getStock, req.getCount()));

        if (updated == 0) {
            log.warn("库存不足: productId={}, 需求={}", req.getProductId(), req.getCount());
            throw new BizException(ErrorCode.STOCK_NOT_ENOUGH);
        }

        Stock stock = stockMapper.selectOne(
                new LambdaQueryWrapper<Stock>().eq(Stock::getProductId, req.getProductId()));
        log.info("扣减库存成功: productId={}, count={}, 剩余={}",
                req.getProductId(), req.getCount(), stock.getStock());
        return new DeductStockResp(stock.getStock());
    }

    @Override
    public Integer getStock(Long productId) {
        Stock stock = stockMapper.selectOne(
                new LambdaQueryWrapper<Stock>().eq(Stock::getProductId, productId));
        return stock == null ? 0 : stock.getStock();
    }
}
