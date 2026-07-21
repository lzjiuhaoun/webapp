# TESTING.md — audit 审计模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 规则匹配逻辑 |
| 集成测试 | Spring Boot Test | Controller |

## Mock 边界

- Elasticsearch → Mock RestTemplate
- 数据库连接 → Mock DB Service
