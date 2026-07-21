# INTERNAL_LOGIC.md — instruction 指令模块

## 核心流程

### 指令下发
```
策略变更(strategy) → InstructionService
                         ↓
         InstructionFactory 封装指令 → Packet
                         ↓
         Socket发送 → manager-center → 目标设备
                         ↓
         记录指令 → InstructionRecord
                         ↓
         等待ACK → InstructionAckListener
                         ↓
         ACK确认 / 超时重试 → 更新指令状态
```

### 指令类型
- 脱敏指令 → mask/maskGroup/maskWater 类
- 水印指令 → markWaterGroup 类
- 策略同步 → strategy 类
- 配置更新 → Device 类

### 指令载荷
- `Payload` 统一载荷结构
- `Packet` 包含指令头 + 载荷
- `PacketAck` 确认响应
