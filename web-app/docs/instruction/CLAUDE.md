# CLAUDE.md — instruction 指令模块

## 模块使命

负责向安全设备下发操作指令：封装策略指令、发送、ACK确认、超时重试和指令记录。

## 技术栈

- MyBatis + MySQL
- Socket 通信（socket-comm-spring-boot-starter）
- Spring WebSocket（ACK回调）

## 文档索引

| 文档 | 路径 |
|------|------|
| 出口契约 | `EXPORTS.md` |
| 内部逻辑 | `INTERNAL_LOGIC.md` |
| 依赖声明 | `DEPENDENCIES.md` |
| 测试策略 | `TESTING.md` |
| 任务书 | `TASKS.md` |
| 验收标准 | `ACCEPTANCE.md` |

## 代码结构

```
com.ankki.webapp/
├── controller/instruction/
│   └── InstructionController          ← 指令管理
├── service/
│   ├── InstructionService/Impl
│   ├── InstructionAckListener          ← ACK监听
│   └── InstructionFactory              ← 指令工厂
├── dao/instruction/
│   ├── InstructionRecordMapper
│   └── InstructionRecordTargetMapper
└── model/instruction/
    ├── Instruction, InstructionAck, InstructionDto
    ├── InstructionRecord, InstructionRecordTarget
    ├── InstractionDetail, InstractionDetailQuery
    ├── Packet, PacketAck, Payload
    ├── Device, DbAssetAck
    ├── markWaterGroup/ (3个类)
    ├── mask/ (6个类)
    ├── maskGroup/ (3个类)
    ├── maskWater/ (3个类)
    └── strategy/ (8个类)
```
