# CLAUDE.md — common-module 模块指令

## 模块角色

公共基础库模块，为所有其他模块提供共享的基础设施和工具。不作为独立进程部署，仅作为 Maven 依赖被其他模块引入。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Framework | 5.3.x | 基础框架 |
| Spring Data Redis | Spring Boot 管理 | Redis 缓存 |
| Apache Zookeeper | 3.4.14 | 服务注册与发现 |
| BouncyCastle | 1.78 | SM4 加密 |
| Lombok | — | 代码生成 |
| HikariCP | Spring Boot 管理 | 连接池 |
| SnakeYAML | — | YAML 解析 |

## Source of Truth

- 源代码: ../src/main/java/com/ankki/common/
- 配置: 通过 ZookeeperConfig 从 yml 加载
- POM: ../pom.xml
- 主类: com.ankki.common.CommonApplication (仅用于依赖注入)

## 核心职责

1. **Zookeeper 客户端**: ZookeeperClient 封装连接、注册、Watch、数据操作
2. **Redis 工具**: RedisUtil 提供 String/Hash/Set/List/ZSet 操作
3. **SM4 加密**: SM4Util 提供对称加密
4. **数据库备份**: AbstractMysqlBackupManager 支持全量/增量备份
5. **Linux 工具**: LinuxUtils 执行 Linux 命令
6. **通用工具**: CommonBeanUtil、DatabaseUrlParserUtil 等

## 被以下模块引用

collection-engine, daemon, kafka-client, manager-center, report-engine, web-app
