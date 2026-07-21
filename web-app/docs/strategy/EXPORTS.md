# EXPORTS.md — strategy 策略管理模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/api` | GET/POST/PUT/DELETE | API策略管理 |
| `/audit` | GET/POST/PUT/DELETE | 审计策略管理 |
| `/ruleGroup` | GET/POST/PUT/DELETE | 脱敏规则组管理 |
| `/desens` | GET/POST/PUT/DELETE | 脱敏策略管理 |
| `/waterRule` | GET/POST/PUT/DELETE | 水印规则管理 |
| `/waterRuleGroup` | GET/POST/PUT/DELETE | 水印规则组管理 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `DesensStrategyService` | 脱敏策略管理 | device, dm-engine |
| `WaterRuleService` | 水印规则管理 | device |
| `ApiStrategyService` | API策略管理 | device |
| `AuditStrategyService` | 审计策略管理 | audit, device |
| `DesenRuleGroupService` | 脱敏规则组管理 | strategy |
| `WaterRuleGroupService` | 水印规则组管理 | strategy |

## 数据模型

| 实体 | 说明 |
|------|------|
| DesensStrategy | 脱敏策略 |
| DesensAlgorithmConfig | 脱敏算法配置 |
| DesenRuleGroup | 脱敏规则组 |
| WaterRule | 水印规则 |
| WaterRuleGroup | 水印规则组 |
| ApiStrategy | API策略 |
| AuditStrategy | 审计策略 |
| AuditRule | 审计规则 |
