package com.djwk.mall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户服务：端口 8081。
 *
 * 练手扩展点：
 *   - 登录后签发 JWT（jjwt），网关校验（TODO）
 *   - 密码加盐 BCrypt
 *   - Redis 会话 / Token 黑名单
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.djwk.mall.user.mapper")
public class MallUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallUserApplication.class, args);
    }
}
