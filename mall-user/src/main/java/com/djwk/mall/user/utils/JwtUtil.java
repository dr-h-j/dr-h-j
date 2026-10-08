package com.djwk.mall.user.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${mall.jwt.secret}")
    private String secret;              // 密钥（至少 32 字节）
    @Value("${mall.jwt.expire-minutes:30}")
    private long expireMinutes;         // 有效期（分钟）

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** 签发 JWT：sub=userId、jti=唯一编号、exp=过期时间 */
    public String create(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .id(UUID.randomUUID().toString())   // jti，黑名单就靠它
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireMinutes * 60_000))
                .signWith(key())
                .compact();
    }

    /** 解析并验签，失败抛异常（过期/签名错都在这里暴露） */
    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload();
    }
}
