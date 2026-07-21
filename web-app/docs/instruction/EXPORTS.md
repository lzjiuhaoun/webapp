# EXPORTS.md — instruction 指令模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/instruction` | GET/POST | 指令查询与下发 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `InstructionService` | 指令下发与管理 | strategy, device |
| `InstructionFactory` | 指令封装 | InstructionService |

## 数据模型

| 实体 | 说明 |
|------|------|
| Instruction | 指令主体 |
| InstructionRecord | 指令记录 |
| InstructionRecordTarget | 指令目标 |
| Packet | 数据包 |
| PacketAck | 确认包 |
| Payload | 指令载荷 |
| InstructionAck | ACK确认 |

## 枚举

| 枚举 | 说明 |
|------|------|
| InstructionType | 指令类型（脱敏、水印、策略同步等）|
| InstructionStatus | 指令状态（待发送、已发送、已确认、失败、超时）|
