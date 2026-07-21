# INTERNAL_LOGIC.md — strategy 策略管理模块

## 核心流程

### 脱敏策略下发
```
创建脱敏策略 → DesensStrategyService
                    ↓
      配置脱敏算法 → DesensAlgorithmConfig
                    ↓
      关联规则组 → DesenRuleGroup
                    ↓
      通过 instruction 模块封装指令
                    ↓
      下发到 DM 设备
                    ↓
      接收 ACK 确认
```

### 水印规则下发
```
创建水印规则 → WaterRuleService
                    ↓
      配置水印算法 → WatermarkAlgorithm 枚举
                    ↓
      通过 instruction 模块封装指令
                    ↓
      下发到目标设备
```

### 审计策略下发
```
创建审计策略 → AuditStrategyService
                    ↓
      配置审计条件 → AuditStrategyRule
                    ↓
      下发到 VS 设备
```

## 枚举
- `MaskMode`, `MaskStyle` — 脱敏模式/样式
- `WatermarkAlgorithm` — 水印算法
- `StrategyStatus` — 策略状态
- `RuleLevel` — 规则级别
