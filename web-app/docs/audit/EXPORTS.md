# EXPORTS.md — audit 审计模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/audit/rule` | GET/POST/PUT/DELETE | 审计规则配置 |
| `/audit/strategy` | GET/POST/PUT/DELETE | 审计策略配置 |
| `/api/audit` | GET/POST/PUT/DELETE | API审计URL管理 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `AuditRuleConfigService` | 审计规则管理 | strategy, device |
| `AuditStrategyConfigService` | 审计策略管理 | strategy |
| `AuditLogSearchService` | 审计日志搜索 | support |

## 数据模型

| 实体 | 说明 |
|------|------|
| AuditRuleConfig | 审计规则配置 |
| AuditStrategyConfig | 审计策略配置 |
| RuleCondition | 规则条件 |
| AuditProtectObject | 审计保护对象 |
| ApiAuditUrl | API审计URL |

## 枚举

| 枚举 | 说明 |
|------|------|
| AuditAction | 审计动作类型 |
| AuditRuleType | 审计规则类型 |
| ConditionOperator | 条件运算符 |
