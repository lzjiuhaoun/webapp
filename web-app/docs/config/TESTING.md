# TESTING.md — config 基础配置模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | Service 层 |
| 集成测试 | Spring Boot Test | Controller |

## Mock 边界

- 后台线程 → 直接调用
- 外部模块 → Mock Service
