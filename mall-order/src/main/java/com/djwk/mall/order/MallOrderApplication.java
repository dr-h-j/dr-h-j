package com.djwk.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单服务：端口 8083。
 *
 * 核心演示链路：下单 = 调 mall-product 扣库存（Feign）+ 本地落订单
 *   - Feign：OpenFeign 远程调用，负载均衡走 Nacos
 *   - Seata：分布式事务占位（默认关闭，开启方法见 README）
 *   - 幂等：TODO Redis SETNX 幂等键（bizId）
 *   - 消息队列：TODO RocketMQ 发送"订单已创建"事件
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.djwk.mall.order.mapper")
public class MallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallOrderApplication.class, args);
    }
}
