# 告警事件表 `alert_result` + `alert_result_audit` 设计

普通规则和组合规则命中后统一写入 `alert_result` 主表，审计日志明细（ID + 席位 IP）写入 `alert_result_audit` 明细表。普通规则产生一条告警事件 + 一条审计明细，组合规则产生一条告警事件 + N 条审计明细。所有展示字段通过审计 ID 从 ES 查询，表中不冗余存储日志字段。

## 关键决策

- **双表明细分离**：将 `AUDIT_LOG_IDS（逗号分隔的 TEXT）` 改为独立明细表 `alert_result_audit`，每行存单条审计日志 ID 及对应的席位 IP。解决：明细数据的结构化查询（按 IP 筛选）、避免 TEXT 字段解析、支持席位 IP 的持久化。
- **不冗余日志字段**：所有展示数据通过审计 ID 从 ES 查询，避免数据不一致
- **冗余 normal_rule_id**：组合规则场景下高频按普通规则分组统计，避免每次通过组合规则表 join 获取
- **IP 独立索引**：`alert_result_audit.ip_address` 建 B-tree 索引，支持精确匹配下的实时页面查询（预期毫秒级）

## 表结构

### alert_result

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT PK | 主键自增 |
| alert_rule_id | INT NOT NULL | 命中规则 ID |
| rule_type | TINYINT NOT NULL | 0=普通, 1=组合 |
| normal_rule_id | INT | 组合规则引用的普通规则 ID |
| alert_level | TINYINT NOT NULL | 告警等级 1=红 2=橙 3=黄 |
| trigger_count | INT DEFAULT 1 | 实际触发次数 |
| create_time | BIGINT NOT NULL | 创建时间 |

### alert_result_audit

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT PK | 主键自增 |
| alert_result_id | INT NOT NULL FK | 告警事件 ID |
| audit_log_id | VARCHAR(36) NOT NULL | 审计源数据全局 ID（UUID） |
| ip_address | VARCHAR(45) NOT NULL | 席位 IP |

索引：`alert_result_audit(ip_address)`，`alert_result_audit(alert_result_id)`
