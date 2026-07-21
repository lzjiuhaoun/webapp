# TESTING.md — situation 态势感知模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 聚合逻辑 |
| 集成测试 | Spring Boot Test | Controller |

## Mock 边界

- 上游模块 → Mock Service
- Elasticsearch → Mock RestTemplate
- 后台线程 → 直接调用聚合方法
