package com.djwk.mall.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 鉴权过滤器占位（TODO）：
 * 练手阶段先放行所有请求，后续接入 JWT 时在这里：
 *   1. 从 Authorization 头解析 Token；
 *   2. 校验签名/有效期（可调 mall-user 或本地公钥）；
 *   3. 把 userId 写入请求头 X-User-Id 透传给下游服务。
 *
 * 健康检查等白名单路径放行：/actuator/**、/user/login 等。
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String[] WHITE_LIST = {
            "/user/login",
            "/user/register",
            "/actuator"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 白名单直接放行
        for (String prefix : WHITE_LIST) {
            if (path.startsWith(prefix)) {
                return chain.filter(exchange);
            }
        }

        // TODO 鉴权逻辑占位：当前直接放行，方便先把链路跑通
        String token = request.getHeaders().getFirst("Authorization");
        if (token == null || token.isBlank()) {
            // 练手阶段不拦截，日志提示即可
            // return unauthorized(exchange, "未携带凭证");
        }

        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
