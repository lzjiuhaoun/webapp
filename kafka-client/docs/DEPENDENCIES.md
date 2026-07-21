# DEPENDENCIES.md — kafka-client 依赖声明

## 内部依赖

| 依赖模块 | 引用文档 | 用途 |
|---------|---------|------|
| `common-module` | ../../docs/common-module/EXPORTS.md | Zookeeper 客户端、Redis 工具、SM4 加密、工具类 |
| `socket-comm` | ../../docs/socket-comm/EXPORTS.md | Socket 客户端（接收 manager-center 转发的指令） |

## 外部库依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot Starter | 2.5.15 | 基础框架 |
| MyBatis | 3.5.10 | ORM |
| PageHelper | 1.4.5 | 分页插件 |
| Druid | 1.1.10 | 数据库连接池 |
| MySQL Connector | 8.0.29 | MySQL 驱动 |
| DM 达梦驱动 | 8.1.3.62 | 国产数据库（备用） |
| Kafka Client | 3.4.0 | Kafka 消费者 |
| Elasticsearch Client | 7.17.20 | ES 客户端 |
| Zookeeper | 3.8.4 | 服务注册、许可证监控 |
| Spring Data Redis | Spring Boot 管理 | Redis 缓存 |
| Lettuce | Spring Boot 管理 | Redis 客户端 |
| Fastjson | 1.2.83 | JSON 解析 |
| Lombok | 1.18.24 | 代码生成 |
| ProGuard | 7.2.0-beta2 | 代码混淆（打包） |
| AppAssembler | — | 打包工具 |
| Commons-IO | — | 文件操作 |
| BouncyCastle | 1.78 | 加密工具 |

## 被以下模块引用

无。kafka-client 是消费方模块，不对外提供服务。

## 消费的外部服务

| 服务 | 用途 |
|------|------|
| Kafka (simp:9092) | 消费 11 个 Topic |
| Elasticsearch (simp:9300) | 审计日志写入 |
| MySQL (localhost:23306/aas_simp) | 资产/配置/报表数据存储 |
| Zookeeper (127.0.0.1:10000) | 服务注册、许可证 Watch |
| Redis (localhost:6379) | 敏感类型缓存 |

## 配置文件

- `src/main/resources/application.properties` — 主配置
- `src/main/resources/mybatis-config.xml` — MyBatis 配置
- `src/main/resources/mapper/*.xml` — 21 个 Mapper XML 映射
- `src/main/assembly/assembly.xml` — tar.gz 打包配置
