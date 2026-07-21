# TESTING.md — risk 风险分析模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | Service 层聚合逻辑 |
| 集成测试 | Spring Boot Test | Controller 端点 |

## Mock 边界

- 上游模块数据 → Mock Service
- Elasticsearch → Mock ElasticsearchRestTemplate
- 定时任务线程 → 直接调用聚合方法
