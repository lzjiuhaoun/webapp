# DEPENDENCIES.md — report-engine 依赖声明

## 内部依赖

| 依赖模块 | 引用文档 | 用途 |
|---------|---------|------|
| `common-module` | ../../docs/common-module/EXPORTS.md | Redis 工具（RedisUtil）、Zookeeper 客户端 |

## 外部库依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot Starter | 2.5.15 | 基础框架 |
| Spring Boot Starter Quartz | — | 定时调度 |
| MyBatis | 3.5.10 | ORM |
| PageHelper | 1.4.5 | 分页插件 |
| Druid | 1.1.10 | 数据库连接池 |
| MySQL Connector | 8.0.29 | MySQL 驱动 |
| Elasticsearch Client | 7.17.20 | ES 客户端 |
| Zookeeper | 3.8.4 | 服务注册 |
| Spring Data Redis (Lettuce) | Spring Boot 管理 | Redis 缓存 |
| Lombok | 1.18.24 | 代码生成 |
| ProGuard | — | 代码混淆（打包） |
| AppAssembler | — | 打包工具 |
| BouncyCastle | — | 加密工具 |
| Jackson | — | JSON 处理 |

## 被以下模块引用

无。report-engine 是报表统计模块，不对外提供服务接口。

## 消费的外部服务

| 服务 | 用途 |
|------|------|
| MySQL `aas_simp` | 报表数据读写、风险表查询 |
| Elasticsearch `simp:9200` | 审计日志查询（SearchAuditLog） |
| Zookeeper (127.0.0.1:10000) | 服务注册 |
| Redis (localhost:6379) | 模型风险缓存 |

## 配置文件

- `src/main/resources/application.properties` — 主配置（ES、MySQL、定时任务、服务名）
- `src/main/resources/mybatis-config.xml` — MyBatis 配置
- `src/main/resources/mapper/*.xml` — 11 个 Mapper XML 映射
- `src/main/assembly/assembly.xml` — tar.gz 打包配置
