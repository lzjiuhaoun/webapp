# CLAUDE.md — kafka-client 模块指令

## 模块角色

Kafka 消费引擎，平台数据中枢。消费 collection-engine 生产的 Kafka 消息，使用策略模式解析审计日志/API审计/资产配置/脱敏任务等各类数据，写入 Elasticsearch 和 MySQL。独立 JVM 进程部署。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| JDK | 1.8 | 运行环境 |
| Spring Boot | 2.5.15 | 基础框架 |
| Kafka | 3.4.0 | 消息消费（SASL_PLAINTEXT） |
| Elasticsearch | 7.17.20 | 审计日志存储 |
| MyBatis | 3.5.10 | MySQL ORM |
| Druid | 1.1.10 | 数据库连接池 |
| Zookeeper | 3.8.4 | 服务注册、许可证监控 |
| Redis | Spring Data Redis | 敏感类型缓存 |
| socket-comm | — | Socket 通信（接收指令） |
| common-module | — | Zookeeper 客户端、Redis 工具 |

## Source of Truth

- 源代码: ../src/main/java/com/ankki/kafkaclient/
- 配置: ../src/main/resources/application.properties
- 数据库映射: ../src/main/resources/mapper/
- POM: ../pom.xml
- 主类: com.ankki.kafkaclient.KafkaClientApplication

## 核心职责

1. **Kafka 多组消费**：6 个消费者组，11 个 Topic，覆盖审计日志/API审计/资产配置/脱敏任务/分类分级/防统一查询
2. **策略解析**：基于 DataAnalysisStrategy 的策略模式，支持 JSON/List/Firewall/API 格式
3. **ES 存储**：aassimp-auditlog / aassimp-apiauditlog 双索引
4. **MySQL 存储**：21 个 Mapper，19 张表，覆盖资产/脱敏/分类/告警等
5. **许可证管控**：ZK Watch 许可证更新、过期拦截、ES 容量限制
6. **定时清理**：每日凌晨 2 点清理已消费 Kafka 消息
7. **敏感数据匹配**：Redis 缓存敏感类型，实时标记审计日志

## 运行配置

- JVM: `-server -Xmx1G -Xms1G` + verbose GC
- 特殊参数: `es.set.netty.runtime.available.processors=false`
- ZK 服务名: `kafkaclient`
- Kafka 消费者线程数: `5`
- maxPollRecords: `500`
- 打包: ProGuard 混淆 + AppAssembler + tar.gz
