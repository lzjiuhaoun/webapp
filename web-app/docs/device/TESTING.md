# TESTING.md — device 安全设备模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | Service 层 |
| 集成测试 | Spring Boot Test | Controller + 数据库 |

## Mock 边界

- 设备通信 → Mock SocketClient
- Kafka 消息 → Mock KafkaTemplate
- Quartz 任务 → 直接调用
