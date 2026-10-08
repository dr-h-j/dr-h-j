-- ============================================================
-- mall-cloud 练手项目数据库初始化脚本
-- 三个独立库：mall_user / mall_product / mall_order（对应三个服务）
-- 通过 docker-compose 挂载本文件时，MySQL 首次启动会自动执行；
-- 手动执行：mysql -uroot -p123456 < sql/init.sql
-- ============================================================

-- ---------- 用户库 ----------
CREATE DATABASE IF NOT EXISTS mall_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mall_user;

DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(32)  NOT NULL COMMENT '用户名（唯一）',
    password    VARCHAR(64)  NOT NULL COMMENT '密码（TODO BCrypt 哈希）',
    nickname    VARCHAR(32)  DEFAULT NULL COMMENT '昵称',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1 正常 0 禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE = InnoDB COMMENT '用户表';

-- 测试用户：admin / 123456
INSERT INTO t_user (username, password, nickname, phone) VALUES
('admin', '123456', '管理员', '13800000000');

-- ---------- 商品/库存库 ----------
CREATE DATABASE IF NOT EXISTS mall_product DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mall_product;

DROP TABLE IF EXISTS t_product;
CREATE TABLE t_product (
    id          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(128)  NOT NULL COMMENT '商品名称',
    image       VARCHAR(255)  DEFAULT NULL COMMENT '商品图片',
    price       DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '单价（元）',
    status      TINYINT       NOT NULL DEFAULT 1 COMMENT '1 上架 0 下架',
    create_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT '商品表';

DROP TABLE IF EXISTS t_stock;
CREATE TABLE t_stock (
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    product_id    BIGINT      NOT NULL COMMENT '商品 ID',
    sku_code      VARCHAR(64) DEFAULT NULL COMMENT 'SKU 编码',
    stock         INT         NOT NULL DEFAULT 0 COMMENT '可用库存',
    frozen_stock  INT         NOT NULL DEFAULT 0 COMMENT '冻结库存（预留）',
    update_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_product (product_id)
) ENGINE = InnoDB COMMENT '库存表';

-- 测试商品 + 库存
INSERT INTO t_product (name, image, price, status) VALUES
('苹果 iPhone 15 Pro',  'https://example.com/iphone15.jpg',  7999.00, 1),
('华为 Mate 60 Pro',    'https://example.com/mate60.jpg',    6999.00, 1),
('小米 14 Ultra',       'https://example.com/mi14.jpg',      5999.00, 1),
('联想小新 Pro 16',     'https://example.com/lenovo.jpg',    5299.00, 1);

INSERT INTO t_stock (product_id, sku_code, stock, frozen_stock) VALUES
(1, 'SKU-0001', 100, 0),
(2, 'SKU-0002', 100, 0),
(3, 'SKU-0003', 100, 0),
(4, 'SKU-0004', 100, 0);

-- ---------- 订单库 ----------
CREATE DATABASE IF NOT EXISTS mall_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mall_order;

DROP TABLE IF EXISTS t_order;
CREATE TABLE t_order (
    id           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no     VARCHAR(40)   NOT NULL COMMENT '业务订单号',
    user_id      BIGINT        NOT NULL COMMENT '下单用户 ID',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额（元）',
    status       TINYINT       NOT NULL DEFAULT 1 COMMENT '1 待支付 2 已支付 3 已发货 4 已完成 5 已取消',
    create_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user (user_id)
) ENGINE = InnoDB COMMENT '订单主表';

DROP TABLE IF EXISTS t_order_item;
CREATE TABLE t_order_item (
    id           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_id     BIGINT        NOT NULL COMMENT '订单 ID',
    product_id   BIGINT        NOT NULL COMMENT '商品 ID',
    product_name VARCHAR(128)  NOT NULL COMMENT '商品名称（快照）',
    price        DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '单价（快照）',
    count        INT           NOT NULL DEFAULT 1 COMMENT '购买数量',
    PRIMARY KEY (id),
    KEY idx_order (order_id)
) ENGINE = InnoDB COMMENT '订单明细表';
