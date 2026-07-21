# EXPORTS.md — risk 风险分析模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/risk/attack` | GET | 攻击风险统计 |
| `/risk/vulnerability` | GET | 漏洞风险统计 |
| `/risk/operation` | GET | 运维风险统计 |
| `/risk/overall` | GET | 总体风险统计 |
| `/risk/data-access` | GET | 数据访问风险统计 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `AttackRiskService` | 攻击风险数据聚合 | situation, report |
| `VulnerabilityRiskService` | 漏洞风险数据聚合 | situation, report |
| `OperationRiskService` | 运维风险数据聚合 | situation, report |
| `OverallRiskService` | 总体风险汇总 | situation, report |
| `DataAccessRiskService` | 数据访问风险聚合 | situation, report |

## 数据模型

| 实体 | 说明 |
|------|------|
| AttackOverview | 攻击风险概览 |
| AttackDb/Ip/Lev/Tool/Type | 攻击维度数据 |
| VulnerabilityRisk | 漏洞风险数据 |
| SystemOperationRisk | 运维风险数据 |
| UserBehavior | 用户行为分析 |
| AccessRisk | 数据访问风险 |
| StatSens | 敏感数据统计 |
