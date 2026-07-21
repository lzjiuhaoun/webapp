# EXPORTS.md — situation 态势感知模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/overall-situation` | GET | 总体态势数据 |
| `/risk-situation` | GET | 风险态势数据 |
| `/safe-model-overview` | GET | 安全模型概览 |
| `/assets-overview` | GET | 资产概览 |
| `/data-flow` | GET | 数据流转图数据 |
| `/event-retrieval` | GET/POST | 事件检索（基于ES）|
| `/health-situation` | GET | 健康态势 |
| `/vulnerability-situation` | GET | 漏洞态势 |
| `/situation` | GET/PUT | 态势配置 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `OverallSituationService` | 总体态势数据聚合 | report |
| `RiskSituationService` | 风险态势聚合 | report |
| `EventSearchService` | ES事件检索 | support |
| `DataFlowOverviewService` | 数据流转图 | report |

## 数据模型

| 实体 | 说明 |
|------|------|
| DailyStatistics | 每日统计 |
| ModelOverview | 安全模型概览 |
| DataFlowBackground | 数据流转背景 |
| VaRecord/WpRecord | 漏洞/威胁记录 |
| HealthSituation | 健康态势 |
| VulnerabilitySituation | 漏洞态势 |
