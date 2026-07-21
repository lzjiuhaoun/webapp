# NON_FUNCTIONAL.md — 全局硬约束

## 禁止项

1. **禁止微服务架构**：系统不使用 Spring Cloud、Docker、Kubernetes 等容器化或微服务框架。各进程为独立 JVM 进程，通过原始 TCP Socket 通信。
2. **禁止消息队列替代 Kafka**：除 Kafka 外，不得使用 RabbitMQ、RocketMQ 等其他消息中间件。
3. **禁止数据库替代 MySQL/DM**：系统内部存储仅使用 MySQL 和 DM 达梦。不得使用 PostgreSQL、Oracle 作为系统内部数据库。
4. **禁止 Elasticsearch 替代**：日志搜索和存储仅使用 Elasticsearch 7.17.x，不得引入 OpenSearch 等其他搜索引擎。
5. **禁止 Java 版本升级**：必须严格保持 JDK 1.8 兼容，不得使用 Java 11+ 的特性（如 `var`、Records 等）。
6. **禁止直接修改 ProGuard 配置**：代码混淆规则已固化，不得随意调整导致混淆失效。
7. **禁止绕过 Zookeeper**：所有服务的注册、发现和健康监控必须通过 Zookeeper，不得使用 Consul、Etcd 等替代。
8. **禁止直接进程间通信**：除 Kafka 消息外，进程间通信必须通过 manager-center 的 Socket 路由，不得直连。

## 强制约束

1. **内存限制**：每个独立进程 JVM 堆内存为 `-Xmx1G -Xms1G`，附带 verbose GC 日志。
2. **代码混淆**：所有独立部署进程必须经过 ProGuard 混淆，Mapper/Model/Util 类除外。
3. **加密要求**：
   - 密码存储必须使用 SM4 加密
   - API 请求/响应必须使用 SM2/RSA 非对称加密
   - 备份文件可选择性使用 SM4 加密
4. **许可证验证**：系统启动时必须通过外部二进制 `client/checklic` 进行许可证校验。
5. **双数据库支持**：MyBatis Mapper 必须同时提供 `mapper/` 和 `dm/mapper/` 两套 XML 配置，分别对应 MySQL 和 DM 达梦。
6. **跨域支持**：所有 REST Controller 必须继承 `BaseController`，默认启用 `@CrossOrigin`。
7. **时区**：统一使用北京时间 (UTC+8)。
8. **日志级别**：生产环境最低为 INFO，DEBUG 仅在开发环境启用。

## 环境约束

1. **部署平台**：CentOS / Kylin-ARM Linux 系统
2. **Maven Profile**：
   - `centos` — 默认激活
   - `kylin-arm` — 麒麟 ARM 环境
3. **HTTPS**：web-app 必须通过 HTTPS (8443) 提供服务，HTTP (8543) 自动重定向。

## 依赖约束

1. **Spring 版本补丁**：Spring Framework 各模块版本已单独 pinned（5.3.20-5.3.27），不得随意升级或降级。
2. **Tomcat 补丁**：Apache Tomcat 已 pinned 至 9.0.86，不得变更。
3. **Kafka SASL**：Kafka 必须使用 SASL_PLAINTEXT + PLAIN 认证，不得裸连。
