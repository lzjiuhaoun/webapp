# DEPENDENCIES.md — manager-center 依赖声明

## 内部依赖

| 依赖模块 | 引用文档 | 用途 |
|---------|---------|------|
| `common-module` | ../../docs/common-module/EXPORTS.md | Zookeeper 客户端、CommonBeanUtil、工具类 |

## 外部库依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot Starter | 2.5.15 | 基础框架 |
| MyBatis | 3.5.10 | ORM |
| Druid | 1.2.4 | 数据库连接池 |
| MySQL Connector | 8.2.0 | MySQL 驱动 |
| DM 达梦驱动 | 8.1.3.62 | 国产数据库（备用） |
| Netty | 4.1.x | 网络库 |
| Jackson | 2.14-2.15 | JSON 处理 |
| Lombok | — | 代码生成 |
| Logback | 1.2.13 | 日志框架 |
| ProGuard | — | 代码混淆（打包） |
| AppAssembler | — | 打包工具 |
| Protobuf | — | 序列化 |
| SnakeYAML | — | YAML 解析 |

## 被以下模块引用

无。manager-center 是消息中转枢纽，不对外提供服务接口（除 Socket 服务外）。

## 消费的外部服务

| 服务 | 用途 |
|------|------|
| MySQL `aas_simp` | `message_routing` 路由表查询 |
| Zookeeper | 服务注册（`/services/managercenter`） |

## 配置文件

- `src/main/resources/application.yml` — 主配置（数据源、Socket、线程池、MyBatis）
- `src/main/resources/mapper/MessageRoutingMapper.xml` — Mapper XML
- `src/main/resources/logback-spring.xml` — 日志配置

## 打包配置

- `src/main/assembly/assembly.xml` — tar.gz 打包
- `proguard-maven-plugin` — ProGuard 混淆

### ProGuard 排除规则
- `com.ankki.managercenter.dao` — Mapper 接口
- `com.ankki.managercenter.model` — 实体类
- `com.ankki.managercenter.util` — 工具类
- `com.ankki.managercenter.*Application` — 主类
- 所有 set/get 方法保留
