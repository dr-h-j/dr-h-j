# mall-cloud · Java 分布式开发练手框架

一个**订单/库存小电商**的 Spring Cloud Alibaba 微服务全家桶框架，用来边玩边学 Java 分布式开发。
所有核心场景都有对应的分布式知识点，代码里留好了 `TODO` 扩展点。

## 一、架构总览

```
                         ┌──────────────────┐
   前端 / Postman ──────►│  mall-gateway    │  网关（8080）：路由 / 跨域 / 鉴权占位 / 限流占位
                         └────────┬─────────┘
                    lb:// 负载均衡（Nacos 注册中心自动发现）
         ┌───────────────┬───────────┴───────────┬───────────────┐
         ▼               ▼                       ▼               │
   mall-user        mall-product            mall-order          │
   用户服务          商品/库存服务            订单服务            │
   (8081)           (8082)                  (8083)              │
     │                │  ▲                    │  Feign          │
     │                │  └──── 扣库存 ◄────────┘  远程调用        │
     ▼                ▼                       ▼                 ▼
 mall_user库      mall_product库          mall_order库       Redis / Nacos / MySQL
```

**下单主链路（核心演示）**：`POST /order/create` → mall-order 用 Feign 调 mall-product 查商品 → 原子扣库存 → 本地落订单 + 明细。

## 二、技术栈

| 分类 | 选型 | 练手知识点 |
|---|---|---|
| 基础 | Java 17 / Spring Boot 3.2.4 / Maven 多模块 | 多模块工程组织 |
| 微服务 | Spring Cloud 2023.0.1 + Spring Cloud Alibaba 2023.0.1.0 | 服务拆分 |
| 注册中心 | Nacos（discovery） | 服务注册与发现、负载均衡 |
| 配置中心 | Nacos（config，已留配置，optional 不阻塞） | 配置统一管理、动态刷新 |
| 网关 | Spring Cloud Gateway（8080） | 路由、跨域、鉴权、限流 |
| 远程调用 | OpenFeign | 声明式 HTTP 调用、超时配置 |
| 数据库 | MySQL 8 + MyBatis-Plus | 分库设计（每服务一库） |
| 缓存 | Redis | 会话、缓存、分布式锁（TODO） |
| 限流熔断 | Sentinel（已引入，规则控制台配置） | 流量控制、熔断降级 |
| 分布式事务 | Seata AT 模式（默认关闭，见进阶玩法） | 跨服务数据一致性 |
| 消息队列 | RocketMQ（TODO 预留） | 异步解耦、削峰 |

## 三、模块与端口

| 模块 | 端口 | 说明 |
|---|---|---|
| mall-gateway | 8080 | 统一入口，路由 `/user/**` `/product/**` `/order/**` |
| mall-user | 8081 | 注册/登录/用户信息，库 `mall_user` |
| mall-product | 8082 | 商品列表/详情/扣库存（原子 SQL 防超卖），库 `mall_product` |
| mall-order | 8083 | 下单（Feign 调商品扣库存），库 `mall_order` |
| mall-common | - | 公共模块：统一响应 `Result`、错误码、全局异常 |

## 四、快速启动

### 1. 起基础设施（Docker）

```bash
docker compose up -d
# Nacos 控制台: http://127.0.0.1:8848/nacos   （账号 nacos / 密码 nacos）
# MySQL 首次启动会自动执行 sql/init.sql 建库建表 + 测试数据
```

> 没有 Docker 的替代方案：
> - Nacos：下载 `nacos-server-2.3.x`，单机启动 `startup.cmd -m standalone`；
> - MySQL/Redis：本地安装后手动执行 `sql/init.sql`，并把各服务 `application.yml` 里的 `MYSQL_*` / `REDIS_*` 环境变量改成你自己的账号密码。

### 2. 启动服务（按依赖顺序，可并行）

```bash
# 四个终端分别执行，或 IDE 里分别启动
mvn -pl mall-gateway spring-boot:run
mvn -pl mall-user   spring-boot:run
mvn -pl mall-product spring-boot:run
mvn -pl mall-order  spring-boot:run
```

> 第一次需先 `mvn install -DskipTests` 把 mall-common 装进本地仓库。

### 3. 跑通整条链路

```bash
# ① 注册 + 登录（mall-user）
curl -X POST http://localhost:8080/user/register -H "Content-Type: application/json" \
  -d '{"username":"loomy","password":"123456","nickname":"小鹿"}'

curl -X POST http://localhost:8080/user/login -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# ② 看商品（mall-product）
curl http://localhost:8080/product/list

# ③ 下单！跨服务调用扣库存（mall-order → mall-product）
curl -X POST http://localhost:8080/order/create -H "Content-Type: application/json" \
  -d '{"userId":1,"productId":1,"count":2}'

# ④ 库存变少了吗？再查一次（应剩 98）
curl http://localhost:8080/product/stock/1
```

预期输出（③）：`{"code":0,"message":"成功","data":{"orderId":1,"orderNo":"M...","totalAmount":"15998.00","remainStock":98}}`

### 4. 不想敲命令？有可视化练手页面

```bash
# 浏览器直接打开（无需启动任何前端服务）
mall-cloud/frontend/index.html
```

页面里集成了：注册 / 登录 / 商品列表 / 库存查询 / 下单 / 订单查询，顶部还有四个服务的**健康探活指示灯**。
网关已配好跨域（CORS），浏览器直连 `http://localhost:8080` 即可。
推荐玩法：检查服务全绿 → 注册拿 userId → 加载商品 → 下单 → 再查库存看剩余数变少。

## 五、练手路线图（对照分享里的分布式知识逐项落地）

| # | 知识点 | 当前状态 | 玩法 |
|---|---|---|---|
| 1 | 服务拆分 / 注册发现 / 负载均衡 | ✅ 已落地 | 启动后去 Nacos 控制台看 4 个服务实例 |
| 2 | 配置中心 | ✅ 骨架已留 | 把 `application.yml` 挪到 Nacos 配置，改配置看动态刷新 |
| 3 | 网关路由 / 跨域 | ✅ 已落地 | 所有请求只走 8080 |
| 4 | 网关鉴权 | ⏳ 占位 | `AuthGlobalFilter` 里接 JWT（jjwt），白名单已留 |
| 5 | 限流熔断 | ⏳ 已引入 | 启动 Sentinel 控制台（`java -Dserver.port=8858 -jar sentinel-dashboard.jar`），配网关/服务流控规则 |
| 6 | 分布式事务 | ⏳ 占位 | 见下「进阶玩法」Seata 部分 |
| 7 | 幂等 | ⏳ TODO | 下单接口加 Redis SETNX 幂等键 |
| 8 | 缓存一致性 | ⏳ TODO | 商品详情加 Cache Aside，Redis 存 + 更新时删 |
| 9 | 分布式锁 | ⏳ TODO | 库存预扣/秒杀场景用 Redisson |
| 10 | 分布式 ID | ⏳ TODO | `generateOrderNo()` 换雪花算法（MyBatis-Plus 自带） |
| 11 | 消息队列 | ⏳ TODO | 下单后发 RocketMQ「订单已创建」，库存异步回补 |
| 12 | 可观测性 | ⏳ TODO | 接入 Micrometer + Prometheus + Grafana，或 SkyWalking |

## 六、进阶玩法：开启 Seata 分布式事务

1. `docker run --name seata-server -p 8091:8091 -e SEATA_IP=127.0.0.1 seataio/seata-server:1.8.0`；
2. Nacos 里新建配置 `seataServer.properties`（默认分组 `SEATA_GROUP`），并建 `seata` 数据库的三张表（undo_log / global_table / branch_table，脚本见 Seata 官方示例）；
3. 取消 `mall-order/application.yml` 里 Seata 配置段注释，设置 `tx-service-group: mall_tx_group`；
4. 打开 `OrderServiceImpl.createOrder` 上的 `@GlobalTransactional` 注释；
5. 手动停掉 mall-product 再下单，观察订单是否被全局回滚（库存恢复）。

## 七、目录结构

```
mall-cloud/
├── pom.xml                  # 父 POM（统一依赖版本）
├── docker-compose.yml       # Nacos + MySQL + Redis 一键起
├── sql/init.sql             # 建库建表 + 测试数据
├── frontend/index.html      # 可视化练手页面（浏览器直接打开）
├── mall-common/             # 统一响应 / 错误码 / 全局异常
├── mall-gateway/            # 网关（WebFlux）
├── mall-user/               # 用户服务
├── mall-product/            # 商品/库存服务
└── mall-order/              # 订单服务（Feign + Seata 占位）
```

> 练手项目，代码尽量直白、注释集中在"为什么"上。开心玩～
