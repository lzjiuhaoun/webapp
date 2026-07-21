# TESTING.md — kafka-client 测试策略

## 测试工具链

- JUnit 4 + Spring Boot Test
- Mockito — Mock Kafka Consumer/ES Client/MySQL/Redis/Zookeeper
- Embedded Kafka — 测试 Kafka 消费流程
- Embedded Elasticsearch — 测试 ES 写入
- H2 Database — 替代 MySQL 进行 Mapper 测试

## 测试目录结构

```
src/test/java/com/ankki/kafkaclient/
├── consumer/           # KafkaConsumerConfig, ConsumerListener 测试
├── process/            # ProcessAuditRecord, ProcessApiAuditRecord 等测试
│   └── strategy/       # 策略模式测试
├── storage/            # DataInToEs, DataInToMysql 测试
├── clean/              # SaticScheduleTask 测试
├── elastic/            # ElasticClientConfig 测试
├── cache/              # DbConfCache 测试
├── sens/               # SensDataMatchHandler 测试
├── mapper/             # MyBatis Mapper 测试
├── utils/              # KafkaUtils, LicenseUtils, CodeCache 测试
└── model/              # 模型类测试
```

## Mock 边界

| 外部依赖 | Mock 方式 | 说明 |
|---------|----------|------|
| Kafka Consumer | Mockito Mock `poll()` | 返回预定义的 ConsumerRecord |
| Kafka Offset | Mockito Mock | 手动提交偏移量 |
| Elasticsearch | Embedded ES 或 Mock | 测试写入操作 |
| MySQL | H2 内存数据库 | 测试 Mapper SQL |
| Redis | Embedded Redis 或 Mock | 测试敏感类型缓存 |
| Zookeeper | Curator Test Server 或 Mock | 测试服务注册/许可证 Watch |
| Linux 命令 | Mockito Mock | 测试许可证验证脚本调用 |
| Socket Client | Mockito Mock | 测试指令接收 |

## 测试重点

1. **策略解析正确性**：JsonDataAnalysis/ListDataAnalysis 对不同格式数据的解析
2. **Topic 路由正确性**：各 Topic 消息路由到正确的处理类
3. **线程池配置**：6 个线程池参数与 application.properties 一致
4. **许可证拦截**：过期许可证跳过消息、ES 容量超限等待
5. **敏感数据匹配**：Redis 缓存匹配标记逻辑
6. **偏移量提交**：手动提交偏移量不丢失消息
7. **批量写入**：ES BulkRequest 和 MySQL batch insert
8. **定时清理**：Kafka AdminClient 删除已消费记录
9. **异常处理**：异常数据记录到 jsonExceptionDataDir
10. **线程安全**：ConcurrentHashMap 缓存、多线程并发处理

## 测试数据准备

- 创建 mock ConsumerRecord 模拟各 Topic 消息
- 使用预定义的 AuditRecord/ApiV1AuditRecord 等模型
- 模拟 ES/Redis/MySQL 的写入结果
