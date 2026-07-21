# TESTING.md — instruction 指令模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 指令封装逻辑 |
| 集成测试 | Spring Boot Test | 指令下发流程 |

## Mock 边界

| 测试类 | 路径 |
|--------|------|
| PacketTest | `test/java/.../model/instruction/PacketTest.java` |
| InstructionServiceImplTest | `test/java/.../service/instruction/impl/InstructionServiceImplTest.java` |

- Socket 通信 → Mock SocketClient
- manager-center → Mock 路由
- 设备响应 → Mock ACK
