# DEPENDENCIES.md — daemon 依赖声明

## 内部依赖

| 依赖模块 | 引用文档 | 用途 |
|---------|---------|------|
| `common-module` | ../../docs/common-module/EXPORTS.md | Zookeeper 客户端、Linux 工具 |

## 外部库依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot Starter | 2.5.15 | 基础框架 |
| Zookeeper | 3.8.4 | 服务监控 |
| Netty | 4.1.x | 网络库 |
| FastJSON | — | JSON 处理 |
| Lombok | — | 代码生成 |
| ProGuard | — | 代码混淆（打包） |

## 消费的外部服务

| 服务 | 用途 |
|------|------|
| Zookeeper | 服务节点 Watch |
