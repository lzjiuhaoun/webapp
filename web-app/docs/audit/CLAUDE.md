# CLAUDE.md — audit 审计模块

## 模块使命

管理数据库审计规则与策略：审计规则配置、审计策略配置、审计日志检索和API审计。

## 技术栈

- MyBatis + MySQL
- Spring Data Elasticsearch（审计日志搜索）

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
├── controller/audit/
│   ├── AuditRuleConfigController         ← 审计规则配置
│   └── AuditStrategyConfigController     ← 审计策略配置
├── controller/api/
│   └── ApiAuditUrlController             ← API审计URL配置
├── service/
│   ├── AuditRuleConfigService/Impl
│   ├── AuditStrategyConfigService/Impl
│   └── AuditLogSearchService/Impl
├── dao/audit + api/
│   ├── AuditRuleConfigMapper
│   ├── AuditStrategyConfigMapper
│   ├── AuditStrategyRuleMapper
│   └── ApiAuditUrlMapper
└── model/audit + api + search/
    ├── AuditRuleConfig, AuditRuleConfigQuery
    ├── AuditStrategyConfig, AuditStrategyConfigQuery
    ├── AuditStrategyConfigUpdate, RuleCondition
    ├── AuditProtectObject, AuditAction
    ├── ApiAuditUrl, ApiAuditRecord
    ├── AuditRecord, AuditRecordCondition
    ├── AuditEventVo
    └── 大量ES Document 模型
```
