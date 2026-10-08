package com.djwk.mall.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表实体。
 */
@Data
@TableName("t_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名（唯一） */
    private String username;

    /** 密码（练手阶段明文存，后续改 BCrypt 哈希） */
    private String password;

    /** 昵称 */
    private String nickname;

    /** 手机号 */
    private String phone;

    /** 状态：1 正常 0 禁用 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
