package com.djwk.mall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 统一网关服务。
 *
 * 端口：8080
 * 路由规则（application.yml）：
 *   /user/**    -> mall-user    用户服务
 *   /product/** -> mall-product 商品/库存服务
 *   /order/**   -> mall-order   订单服务
 *
 * 练手扩展点：
 *   - AuthGlobalFilter：JWT 鉴权（TODO）
 *   - Sentinel 网关限流：控制台配置
 *   - 跨域：CorsWebFilter（已内置）
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallGatewayApplication.class, args);
    }
}
