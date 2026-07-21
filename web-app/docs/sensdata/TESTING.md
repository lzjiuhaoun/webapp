# TESTING.md — sensdata 敏感数据模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 识别算法、统计逻辑 |
| 集成测试 | Spring Boot Test | Controller + 数据库 |

## Mock 边界

- dm-engine 识别 → Mock 算法入口
- 数据库连接 → Mock 数据库适配
- Quartz 任务 → 直接调用
