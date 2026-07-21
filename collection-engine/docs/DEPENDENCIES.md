# DEPENDENCIES.md — collection-engine 依赖声明

## 内部依赖

| 依赖模块 | 引用文档 | 用途 |
|---------|---------|------|
| `common-module` | ../../docs/common-module/EXPORTS.md | Zookeeper 客户端、Redis 工具、工具类 |

## 外部库依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot Starter | 2.5.15 | 基础框架 |
| Netty | 4.1.x | UDP 服务器 |
| syslog4j | 0.9.46 | Syslog 解析 |
| Kafka Client | 3.4.0 | Kafka 生产者 |
| MyBatis | 3.5.10 | MySQL ORM |
| Druid | 1.1.10 | 数据库连接池 |
| MySQL Connector | 8.0.29 | MySQL 驱动 |
| Zookeeper | 3.8.4 | 服务注册 |
| Lombok | 1.18.24 | 代码生成 |
| ProGuard | 7.2.0-beta2 | 代码混淆 |
| AppAssembler | — | 打包工具 |
| JSON Utils | — | JSON 序列化 |

## 被以下模块引用

无。collection-engine 是数据采集方，不对外提供服务接口（除 UDP 服务外）。

## 消费的外部服务

| 服务 | 用途 |
|------|------|
| Kafka (simp:9092) | 生产消息到多个 Topic |
| MySQL `aas_simp` | 安全设备配置存储、Topic 映射 |
| Zookeeper (127.0.0.1:10000) | 服务注册 |

## 配置文件

- `src/main/resources/application.yml` — 主配置（数据源、服务名、MyBatis）
- `src/main/resources/syslogconf.properties` — Syslog 配置（端口、Topic、线程池）
- `src/main/resources/mapper/*.xml` — MyBatis XML 映射
- `src/main/assembly/assembly.xml` — tar.gz 打包配置

## ProGuard 排除规则

- `com.ankki.simpsyslogserver.dao` — Mapper 接口
- `com.ankki.simpsyslogserver.model` — 实体类
- `com.ankki.simpsyslogserver.serverthread` — 线程类
- `com.ankki.simpsyslogserver.util` — 工具类
- `com.ankki.simpsyslogserver.SimpsyslogserverApplication` — 主类
- `com.ankki.simpsyslogserver.SystemStart` — 启动类
- 所有 set/get 方法保留
