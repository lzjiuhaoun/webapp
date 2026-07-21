# DEPENDENCIES.md — situation 态势感知模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| risk | `AttackRiskService`, `VulnerabilityRiskService` 等 | >= v1.0.0 |
| db-assets | `DbAssetsService` — 资产数据 | >= v1.0.0 |
| device | `CmDevService` — 设备数据 | >= v1.0.0 |
| sensdata | `SensDataSummaryService` — 敏感数据统计 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| org.springframework.data:spring-data-elasticsearch | ES查询 |
| com.ankki:common-module | 公共工具类 |
