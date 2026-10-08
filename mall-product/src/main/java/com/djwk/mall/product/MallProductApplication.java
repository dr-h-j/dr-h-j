package com.djwk.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 商品/库存服务：端口 8082。
 *
 * 练手扩展点：
 *   - 库存扣减目前用原子 UPDATE（乐观思路），可升级为 Redis Lua 预扣 + 异步回补（TODO）
 *   - 商品详情可加 Redis 缓存（Cache Aside）演示缓存一致性（TODO）
 *   - 分布式锁 Redisson（TODO）
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.djwk.mall.product.mapper")
public class MallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }
}
