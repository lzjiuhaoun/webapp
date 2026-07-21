# CLAUDE.md — strategy 策略管理模块

## 模块使命

管理脱敏策略、水印规则、API策略和审计策略。策略创建后可通过 instruction 模块下发到安全设备执行。

## 技术栈

- MyBatis + MySQL
- dm-engine 脱敏引擎
- Socket 通信（指令下发）

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
├── controller/strategy/
│   ├── ApiStrategyController        ← /api
│   ├── AuditStrategyController      ← /audit
│   ├── DesenRuleGroupController     ← /ruleGroup
│   ├── DesensStrategyController     ← /desens
│   ├── WaterRuleController          ← /waterRule
│   └── WaterRuleGroupController     ← /waterRuleGroup
├── service/
│   ├── ApiStrategyService/Impl
│   ├── AuditStrategyService/Impl
│   ├── DesensStrategyService/Impl
│   ├── DesenRuleGroupService/Impl
│   ├── WaterRuleService/Impl
│   └── WaterRuleGroupService/Impl
├── dao/strategy/
│   ├── ApiStrategyMapper, AuditRuleMapper, AuditStrategyInfoMapper
│   ├── AuditStrategyRuleRelMapper, DesenRuleGroupMapper
│   ├── DesensAlgorithmConfigMapper, DesensStrategyMapper
│   ├── StrategyDbconfConfigMapper, StrategyDeliveryConfigMapper
│   ├── WaterRuleGroupMapper, WaterRuleMapper
└── model/strategy/
    ├── api/ (ApiStrategy, ApiRule, ApiRuleCondition)
    ├── audit/ (AuditRule, AuditStrategy, AuditStrategyRule, etc.)
    ├── desens/ (DesensStrategy, DesensAlgorithmConfig, DesenRuleGroup, etc.)
    └── waterRule/ (WaterRule, WaterRuleGroup, etc.)
```
