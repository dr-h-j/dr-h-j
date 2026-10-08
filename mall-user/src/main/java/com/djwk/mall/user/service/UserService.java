package com.djwk.mall.user.service;

import com.djwk.mall.user.dto.LoginReq;
import com.djwk.mall.user.dto.LoginResp;
import com.djwk.mall.user.dto.RegisterReq;
import com.djwk.mall.user.entity.User;

/**
 * 用户服务接口。
 */
public interface UserService {

    /** 注册 */
    User register(RegisterReq req);

    /** 登录 */
    LoginResp login(LoginReq req);

    /** 按 ID 查询 */
    User getById(Long id);

    /** 登出 */
    void logout(String token);
}
