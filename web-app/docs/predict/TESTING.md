# TESTING.md — predict 态势预测模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 预测算法 |
| 集成测试 | Spring Boot Test | Controller |

## Mock 边界

- 上游模块数据 → Mock Service
