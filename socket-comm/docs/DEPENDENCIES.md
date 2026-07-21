# DEPENDENCIES.md — socket-comm 依赖声明

## 外部库依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot Starter | 2.5.15 | 基础框架 |
| Lombok | — | 代码生成 |

## 内部依赖

无。socket-comm 是基础库，不依赖项目内其他模块。

## 被以下模块依赖

- kafka-client
- web-app
- manager-center（有独立实现但共享协议）
