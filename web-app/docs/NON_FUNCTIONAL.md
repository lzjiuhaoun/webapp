# NON_FUNCTIONAL.md — 全局硬约束

## 所有模块必须遵守的技术约束

**版本：** v1.0.0

---

## 1. 禁止项

| 禁止项 | 说明 |
|-------|------|
| 禁止引入 Spring Boot Actuator | 已排除，不使用 /actuator 端点 |
| 禁止引入 Log4j 2.x | 统一使用 Logback（logback-core 1.2.13 + logback-classic）|
| 禁止使用 SnakeYAML 默认版本 | 各模块排除 snakeyaml，统一由 pom 控制版本（2.0）|
| 禁止微服务架构 | 单体 Spring Boot 应用，不拆分服务 |
| 禁止引入 RabbitMQ / RocketMQ | 消息通信统一使用 Kafka |
| 禁止引入 Redis | 缓存使用 EhCache |
| 禁止修改 common-module | 公共工具类只能在 common-module 中修改 |
| 禁止修改 socket-comm | Socket 通信库只能在 socket-comm 模块中修改 |

## 2. 强制版本

| 组件 | 强制版本 | 原因 |
|------|---------|------|
| Java | 1.8 | 项目基础要求 |
| Spring Boot | 2.5.15 | 框架版本 |
| Spring Framework | 5.3.27 | 安全升级 |
| Tomcat | 9.0.74+ | 嵌入式/外部容器 |
| MyBatis | 3.5.10 | 持久层框架 |
| Jackson | 2.15.3 | JSON 序列化 |
| Netty | 4.1.92.Final+ | Socket 通信 |
| Elasticsearch | 7.17.20 | 搜索引擎 |
| Kafka Client | 匹配 Spring Boot 2.5.x | 消息队列 |

## 3. 安全约束

| 约束 | 说明 |
|------|------|
| HTTPS 强制 | 服务端默认启用 SSL（TLSv1.2），端口 8443 |
| CAS 认证 | 所有用户登录走 CAS 单点登录流程 |
| IP 白名单 | 管理后台支持 IP 白名单限制（LoginIpConfig）|
| API 加密 | 支持 API 响应数据加密 |
| 敏感数据不落日志 | 密码、密钥等敏感信息不得写入日志文件 |

## 4. 编码规范

| 规范 | 说明 |
|------|------|
| 包命名 | `com.ankki.webapp.[module].[layer]` |
| Controller | 使用 `@RestController`，路径前缀与业务域对应 |
| Service | 接口 + Impl 模式，接口命名 `XxxService`，实现命名 `XxxServiceImpl` |
| Mapper | 使用 `@Mapper` 注解，XML 放 `resources/mapper/[module]/*.xml` |
| 实体类 | 统一放在 `com.ankki.webapp.model` 下，按子域分包 |
| 全局异常 | 通过 `GlobalExceptionHandler`（@RestControllerAdvice）统一处理 |

## 5. 部署约束

| 约束 | 说明 |
|------|------|
| 打包方式 | jar（默认）或 war（可切换）|
| 部署环境 | CentOS / 麒麟 ARM（通过 Maven profile 区分）|
| 外部依赖进程 | manager-center（port 9090）、MySQL（port 23306）、ES（port 9200）、Kafka（port 9092）|
| 数据库兼容 | MySQL（主）、达梦、Oracle、PostgreSQL、DB2、SQLServer、Hive、MongoDB、HBase 等 |

## 6. 日志规范

| 规范 | 说明 |
|------|------|
| 日志框架 | Logback（logback-spring.xml 配置）|
| 日志级别 | 生产环境 INFO+，开发环境 DEBUG+ |
| 日志文件 | 按模块滚动，保留天数和大小限制参见 logback-spring.xml |

## 7. 多数据库适配

| 要求 | 说明 |
|------|------|
| 数据库工厂 | 通过 `DatabaseFactory` 创建对应数据库操作对象 |
| 数据库实现 | MysqlDatabase, OracleDatabase, PostgreDatabase, DmDatabase, SqlServerDatabase, HiveDatabase, HbaseDatabase, MongoDbDatabase, HighGoDatabase, Db2Database 等 |
| MyBatis Mapper | 主版本在 `resources/mapper/`，达梦版本在 `resources/dm/mapper/` |
