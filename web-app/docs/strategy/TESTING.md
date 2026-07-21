# TESTING.md — strategy 策略管理模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | Service 层 |
| 集成测试 | Spring Boot Test | Controller |

## Mock 边界

- instruction 下发 → Mock InstructionService
- 设备通信 → Mock SocketClient
- dm-engine → Mock 算法执行
