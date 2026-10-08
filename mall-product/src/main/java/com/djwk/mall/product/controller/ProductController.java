package com.djwk.mall.product.controller;

import com.djwk.mall.common.Result;
import com.djwk.mall.product.dto.DeductStockReq;
import com.djwk.mall.product.dto.DeductStockResp;
import com.djwk.mall.product.entity.Product;
import com.djwk.mall.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品/库存服务接口。网关路由：/product/**
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /** 商品列表 */
    @GetMapping("/list")
    public Result<List<Product>> list() {
        return Result.ok(productService.listProducts());
    }

    /** 商品详情 */
    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable("id") Long id) {
        return Result.ok(productService.getProduct(id));
    }

    /** 扣减库存（order 服务 Feign 调用） */
    @PostMapping("/stock/deduct")
    public Result<DeductStockResp> deductStock(@Valid @RequestBody DeductStockReq req) {
        return Result.ok(productService.deductStock(req));
    }

    /** 查询库存 */
    @GetMapping("/stock/{id}")
    public Result<Integer> stock(@PathVariable("id") Long id) {
        return Result.ok(productService.getStock(id));
    }
}
