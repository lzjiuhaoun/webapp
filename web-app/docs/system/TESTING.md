# TESTING.md — system 系统管理模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | Service 层 |
| 集成测试 | Spring Boot Test | Controller 端点 |

## Mock 边界

- Quartz 任务调度 → 直接调用目标方法
- Socket 通信 → Mock SocketClient
- License 文件 → 使用测试 License
