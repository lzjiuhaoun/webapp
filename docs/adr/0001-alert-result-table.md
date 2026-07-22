# 告警事件表 `alert_result` 设计

普通规则和组合规则命中后统一写入单表 `alert_result`，以 `rule_type` 区分。普通规则命中存单个审计 ID，组合规则命中存逗号分隔的多个审计 ID 及实际触发次数。所有展示字段通过审计 ID 从 ES 查询，表中不冗余存储日志字段。

## 关键决策

- **单表而非双表分离**：普通命中与组合命中差异小（仅审计 ID 数量、触发次数字段），同一张表简化查询逻辑
- **逗号分隔而非 JSON/关联表**：审计 ID 为纯数字，逗号分隔的字符串查询简便（`FIND_IN_SET`），无需解析 JSON
- **不冗余日志字段**：所有展示数据通过审计 ID 从 ES 查询，避免数据不一致
- **冗余 normal_rule_id**：组合规则场景下高频按普通规则分组统计，避免每次通过组合规则表 join 获取

## 表结构

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT PK | 主键自增 |
| alert_rule_id | INT NOT NULL | 命中规则 ID |
| rule_type | TINYINT NOT NULL | 0=普通, 1=组合 |
| audit_log_ids | TEXT NOT NULL | 审计源数据全局 ID，逗号分隔 |
| normal_rule_id | INT | 组合规则引用的普通规则 ID |
| alert_level | TINYINT NOT NULL | 告警等级 1=红 2=橙 3=黄 |
| trigger_count | INT DEFAULT 1 | 实际触发次数 |
| create_time | BIGINT NOT NULL | 创建时间 |
