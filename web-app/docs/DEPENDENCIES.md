# DEPENDENCIES.md — web-app 依赖声明

## 内部依赖

| 依赖模块 | 引用文档 | 用途 |
|---------|---------|------|
| common-module | ../../docs/common-module/EXPORTS.md | Zookeeper、Redis、SM4、备份、工具类 |
| socket-comm | ../../docs/socket-comm/EXPORTS.md | Socket 通信 |

## 外部库依赖（主要）

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot Starter Web | 2.5.15 | Web 框架 |
| Spring Boot Starter WebSocket | — | WebSocket |
| Spring Boot Starter Quartz | — | 定时调度 |
| Swagger (springfox) | 3.0.0 | API 文档 |
| CAS Client | 3.2.1 | 单点登录 |
| MyBatis | 3.5.10 | ORM |
| PageHelper | 1.4.5 | 分页 |
| Druid | 1.2.4 | 连接池 |
| MySQL Connector | 8.2.0 | MySQL 驱动 |
| DM 达梦驱动 | 8.1.3.62 | 国产数据库 |
| Oracle JDBC | — | Oracle 连接 |
| PostgreSQL JDBC | — | PostgreSQL 连接 |
| SQL Server JDBC | — | SQL Server 连接 |
| DB2 JDBC | — | DB2 连接 |
| MongoDB Driver | — | MongoDB 连接 |
| Hive JDBC | — | Hive 连接 |
| BouncyCastle | 1.78 | SM2/SM4 加密 |
| iText | 5.5.13.3 | PDF 生成 |
| Apache POI | 5.2.2 | Excel 生成 |
| snmp4j | 2.6.2 | SNMP |
| dnsjava | 2.1.8 | DNS |
| Apache Camel FTP | 3.14.0 | FTP/SFTP |
| Fastjson | 1.2.83 | JSON |
| Hutool | 5.8.20 | 工具集 |
| Google Guava | 32.0.0-jre | 工具集 |
| commons-net | 3.9.0 | FTP |
| commons-fileupload | 1.3.1 | 文件上传 |

## 消费的外部服务

| 服务 | 用途 |
|------|------|
| MySQL aas_simp | 主数据库 |
| Elasticsearch | 审计日志检索 |
| Kafka | 消息生产/消费 |
| Zookeeper | 服务注册、Watch |
| Redis | 缓存 |
| CAS Server | 认证 |
