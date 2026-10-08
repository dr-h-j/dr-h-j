package com.djwk.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.djwk.mall.common.BizException;
import com.djwk.mall.common.ErrorCode;
import com.djwk.mall.user.dto.LoginReq;
import com.djwk.mall.user.dto.LoginResp;
import com.djwk.mall.user.dto.RegisterReq;
import com.djwk.mall.user.entity.User;
import com.djwk.mall.user.mapper.UserMapper;
import com.djwk.mall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户服务实现。
 * 练手阶段：
 *   - 密码明文存储（TODO BCrypt）
 *   - Token 用 UUID 模拟（TODO JWT + Redis 会话）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User register(RegisterReq req) {
        Long exists = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, req.getUsername()));
        if (exists != null && exists > 0) {
            throw new BizException(ErrorCode.USER_NAME_EXISTS);
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword()); // TODO BCrypt 哈希
        user.setNickname(req.getNickname() == null ? req.getUsername() : req.getNickname());
        user.setPhone(req.getPhone());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
        log.info("用户注册成功: id={}, username={}", user.getId(), user.getUsername());
        return user;
    }

    @Override
    public LoginResp login(LoginReq req) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, req.getUsername()));
        if (user == null || !user.getPassword().equals(req.getPassword())) {
            throw new BizException(ErrorCode.USER_PASSWORD_ERROR);
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BizException(ErrorCode.FORBIDDEN, "账号已被禁用");
        }
        // TODO 换 JWT，并把 token 存 Redis 做会话管理
        String token = UUID.randomUUID().toString().replace("-", "");
        log.info("用户登录成功: id={}", user.getId());
        return new LoginResp(user.getId(), user.getUsername(), token);
    }

    @Override
    public User getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }
}
