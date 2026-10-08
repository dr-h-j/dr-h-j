package com.djwk.mall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应（练手阶段返回模拟 token，后续换 JWT）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResp {

    private Long userId;

    private String username;

    /** 模拟 Token，TODO 换 JWT */
    private String token;
}
