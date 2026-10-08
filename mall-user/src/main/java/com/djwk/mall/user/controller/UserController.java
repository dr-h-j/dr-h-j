package com.djwk.mall.user.controller;

import com.djwk.mall.common.Result;
import com.djwk.mall.user.dto.LoginReq;
import com.djwk.mall.user.dto.LoginResp;
import com.djwk.mall.user.dto.RegisterReq;
import com.djwk.mall.user.entity.User;
import com.djwk.mall.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务接口。网关路由：/user/**
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 注册 */
    @PostMapping("/register")
    public Result<Long> register(@Valid @RequestBody RegisterReq req) {
        return Result.ok(userService.register(req).getId());
    }

    /** 登录 */
    @PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq req) {
        return Result.ok(userService.login(req));
    }

    /** 用户详情 */
    @GetMapping("/{id}")
    public Result<User> detail(@PathVariable("id") Long id) {
        return Result.ok(userService.getById(id));
    }
}
