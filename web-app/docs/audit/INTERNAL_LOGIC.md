# INTERNAL_LOGIC.md — audit 审计模块

## 核心流程

### 审计规则匹配
```
数据库操作事件 → 审计规则引擎
                     ↓
        匹配 AuditRuleConfig 规则条件
                     ↓
        触发审计动作 → 记录日志 → ES存储
                     ↓
        匹配告警条件 → 通知 system 模块
```

### 审计日志搜索
- 基于 Elasticsearch 的全文检索
- 支持多条件组合：用户、IP、操作类型、时间范围等
- `AuditLogSearchService` 提供统一搜索入口

### API审计
- `ApiAuditUrlController` 配置API审计URL
- 每个API请求记录到ES
- 支持按URL、方法、参数等维度检索
