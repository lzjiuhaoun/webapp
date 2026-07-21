# TESTING.md — support 支撑模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 日志处理、拓扑生成 |
| 集成测试 | Spring Boot Test | WebSocket端点 |

## Mock 边界

- Elasticsearch → Mock RestTemplate
- WebSocket → Mock WebSocketSession
- 缓存 → Mock Cache
