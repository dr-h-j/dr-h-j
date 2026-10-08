package com.djwk.mall.order.feign;

import com.djwk.mall.common.Result;
import com.djwk.mall.order.dto.ProductInfo;
import com.djwk.mall.order.dto.ProductStockResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.djwk.mall.order.dto.DeductStockReq;

/**
 * 商品服务 Feign 客户端：远程调用 mall-product。
 *
 * fallback 用法（TODO）：加 fallbackFactory 演示服务降级 + Sentinel 熔断。
 */
@FeignClient(name = "mall-product", contextId = "productClient")
public interface ProductClient {

    /** 商品详情 */
    @GetMapping("/product/{id}")
    Result<ProductInfo> getProduct(@PathVariable("id") Long id);

    /** 扣减库存 */
    @PostMapping("/product/stock/deduct")
    Result<ProductStockResp> deductStock(@RequestBody DeductStockReq req);
}
