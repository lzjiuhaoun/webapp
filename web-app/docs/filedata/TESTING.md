# TESTING.md — filedata 文件数据模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 文件解析逻辑 |
| 集成测试 | Spring Boot Test | Controller |

## Mock 边界

- 文件I/O → Mock FileSystem
- 敏感识别 → Mock sensdata Service
