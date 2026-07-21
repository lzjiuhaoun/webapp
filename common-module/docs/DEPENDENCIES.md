# DEPENDENCIES.md — common-module 依赖声明

## 外部库依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Framework | 5.3.x | 基础框架 |
| Spring Data Redis | Spring Boot 管理 | Redis 缓存 |
| Apache Zookeeper | 3.4.14 | 服务注册与发现 |
| BouncyCastle | 1.78 | SM4 加密 |
| Lombok | — | 代码生成 |
| HikariCP | Spring Boot 管理 | 连接池 |
| SnakeYAML | — | YAML 解析 |

## 内部依赖

无。common-module 是基础库，不依赖项目内其他模块。

## 被以下模块依赖

- collection-engine
- daemon
- kafka-client
- manager-center
- report-engine
- web-app
