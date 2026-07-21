# CLAUDE.md — collection-engine 模块指令

## 模块角色

采集引擎：接收数据库安全设备通过 UDP Syslog 上报的审计日志，按数据类型分类后生产到对应 Kafka Topic。独立 JVM 进程部署。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| JDK | 1.8 | 运行环境 |
| Spring Boot | 2.5.15 | 基础框架 |
| Netty | 4.1.x | UDP 服务器 |
| syslog4j | 0.9.46 | Syslog 解析 |
| Kafka Client | 3.4.0 | Kafka 生产者 |
| MyBatis | 3.5.10 | MySQL ORM |
| Druid | 1.1.10 | 数据库连接池 |
| MySQL Connector | 8.0.29 | MySQL 驱动 |
| Zookeeper | 3.8.4 | 服务注册 |
| ProGuard | 7.2.0-beta2 | 代码混淆 |

## Source of Truth

- 源代码: ../src/main/java/com/ankki/simpsyslogserver/
- 配置: ../src/main/resources/application.yml
- 数据库映射: ../src/main/resources/mapper/
- 配置文件: ../src/main/resources/syslogconf.properties
- POM: ../pom.xml
- 主类: com.ankki.simpsyslogserver.SimpsyslogserverApplication

## 核心职责

1. **UDP Syslog 接收**：Netty UDP 服务器接收安全设备 Syslog
2. **数据分类**：按 dataType (0-9) 分类到不同 Kafka Topic
3. **Kafka 生产**：批量发送到 Kafka（SASL_PLAINTEXT 认证）
4. **设备配置管理**：MySQL 存储安全设备配置和 Topic 映射
5. **Sangfor 格式转换**：将深信服设备数据转换为标准格式

## 运行配置

- JVM: `-Xmx1G -Xms1G` + verbose GC
- UDP 端口: 9514 (Netty)
- ZK 服务名: `syslogserver`
- 线程池: core=5, max=5, queue=150
- 打包: ProGuard 混淆 + AppAssembler
