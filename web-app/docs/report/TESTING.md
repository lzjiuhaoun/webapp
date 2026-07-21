# TESTING.md — report 报表模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 导出逻辑 |
| 集成测试 | Spring Boot Test | Controller |

## Mock 边界

- 上游模块数据 → Mock Service
- 文件输出 → Mock OutputStream
- BI任务线程 → 直接调用
