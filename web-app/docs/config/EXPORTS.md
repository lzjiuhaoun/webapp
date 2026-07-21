# EXPORTS.md — config 基础配置模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/config/province-city` | GET/POST/PUT/DELETE | 省市区配置 |
| `/config/logic-area` | GET/POST/PUT/DELETE | 逻辑区域 |
| `/config/organization` | GET/POST/PUT/DELETE | 组织架构 |
| `/config/manager` | GET/POST/PUT/DELETE | 管理员配置 |
| `/config/physical-pos` | GET/POST/PUT/DELETE | 物理位置 |
| `/config/security-models` | GET/POST/PUT/DELETE | 安全模型 |
| `/config/security-rules` | GET/POST/PUT/DELETE | 安全规则 |
| `/config/suspicious-group` | GET/POST/PUT/DELETE | 嫌疑分组 |
| `/config/suspicious-list` | GET/POST/PUT/DELETE | 嫌疑列表 |
| `/config/warn-strategy` | GET/POST/PUT/DELETE | 告警策略 |
| `/config/data-reporting` | GET/POST/PUT/DELETE | 数据上报 |
| `/alert-access-list` | GET/POST/PUT/DELETE | 告警黑白名单 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `OrganizationConfigService` | 组织架构管理 | 全模块 |
| `LogicAreaService` | 逻辑区域管理 | db-assets |
| `PhysicalPosService` | 物理位置管理 | db-assets |
| `ProvinceCityService` | 省市区数据 | 全模块 |
| `SuspiciousListService` | 嫌疑列表管理 | risk, device |
| `SecurityModelsService` | 安全模型管理 | risk |
| `WarnStrategyConfService` | 告警策略配置 | system |
| `DataReportingService` | 数据上报管理 | system |

## 数据模型

| 实体 | 说明 |
|------|------|
| OrganizationConfig | 组织架构 |
| LogicAreaConfig | 逻辑区域 |
| PhysicalAddrConfig | 物理位置 |
| SecurityModels | 安全模型 |
| SecurityRules | 安全规则 |
| SuspiciousGroup | 嫌疑分组 |
| SuspiciousList | 嫌疑列表 |
| WarnStrategyConf | 告警策略 |
| DataReportingConf | 数据上报配置 |
| AccessList | 告警黑白名单 |
| AccessListUser | 黑白名单用户明细 |
