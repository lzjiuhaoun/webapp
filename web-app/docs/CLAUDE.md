# CLAUDE.md — web-app 模块指令

## 模块角色

Web 应用：平台的主要用户界面，提供 70+ REST API 控制器、WebSocket 推送、CAS 单点登录、Quartz 调度、文件管理和报表导出等功能。独立 JVM 进程部署，WAR 包形式。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| JDK | 1.8 | 运行环境 |
| Spring Boot | 2.5.15 | 基础框架 |
| Tomcat 9.0.86 | — | 嵌入式 Web 服务器 |
| Swagger 3.0.0 | — | API 文档 |
| CAS | 3.2.1 | 单点登录 |
| Quartz | Spring Boot Quartz | 定时调度 |
| MyBatis | 3.5.10 | ORM |
| Druid | 1.2.4 | 连接池 |
| Zookeeper | 3.8.4 | 服务注册 |
| socket-comm | — | Socket 通信 |
| common-module | — | 公共库 |
| Elasticsearch | 7.17.20 | 审计日志检索 |
| Kafka | 3.4.0 | 消息生产 |
| iText | 5.5.13.3 | PDF 生成 |
| Apache POI | 5.2.2 | Excel 导出 |
| Netty | 4.1.x | 网络库 |
| SNMP4J | 2.6.2 | SNMP 监控 |
| Pentaho Kettle | — | 数据转换 |
| HBase | — | 大数据 |
| OSHI | — | 系统信息 |
| Hutool | 5.8.20 | 工具集 |
| BouncyCastle | 1.78 | SM2/SM4 加密 |

## Source of Truth

- 源代码: ../src/main/java/com/ankki/webapp/
- 配置: ../src/main/resources/application.yml + application-dev.yml/application-prod.yml
- 数据库映射: ../src/main/resources/mapper/
- POM: ../pom.xml
- 主类: com.ankki.webapp.WebAppApplication

## 核心职责

1. REST API: 70+ 控制器，覆盖用户管理、审计检索、风险评估、资产发现等
2. WebSocket: 实时消息推送
3. CAS SSO: 单点登录认证
4. Quartz 调度: JDBC 模式定时任务
5. 文件管理: FTP/SFTP、文件上传下载
6. 报表导出: PDF/Excel 生成
7. API 加密: SM2/RSA 非对称加密
8. 多数据库支持: 可连接 Oracle、PostgreSQL、DB2、SQL Server、Sybase、Informix、DM、Kingbase 等

## 运行配置

- HTTPS 端口: 8443 (TLSv1.2)
- HTTP 端口: 8543（重定向到 HTTPS）
- Socket 端口: 9095
- ZK 服务名: `webapp`
- 打包: WAR（可部署到外部 Tomcat）
