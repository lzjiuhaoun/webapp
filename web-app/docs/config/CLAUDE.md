# CLAUDE.md — config 基础配置模块

## 模块使命

管理系统基础配置：组织架构、逻辑区域、物理位置、安全模型、安全规则、嫌疑列表、告警策略和数据上报配置。

## 技术栈

- MyBatis + MySQL
- 后台线程（数据同步）

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
├── controller/config/
│   ├── ProvinceCityController          ← 省市区配置
│   ├── LogicAreaController             ← 逻辑区域
│   ├── OrganizationConfigController    ← 组织架构
│   ├── ManagerConfigController         ← 管理员配置
│   ├── PhysicalPosController           ← 物理位置
│   ├── SecurityModelsController        ← 安全模型
│   ├── SecurityRulesController         ← 安全规则
│   ├── SuspiciousGroupController       ← 嫌疑分组
│   ├── SuspiciousListController        ← 嫌疑列表
│   ├── WarnStrategyConfController      ← 告警策略
│   └── DataReportingConfController     ← 数据上报配置
├── service/
│   ├── ProvinceCityService/Impl
│   ├── LogicAreaService/Impl
│   ├── OrganizationConfigService/Impl
│   ├── ManagerConfigService/Impl
│   ├── PhysicalPosService/Impl
│   ├── SecurityModelsService/Impl
│   ├── SecurityRulesService/Impl
│   ├── SuspiciousGroupService/Impl
│   ├── SuspiciousListService/Impl
│   ├── WarnStrategyConfService/Impl
│   ├── DataReportingService/Impl
│   ├── GetManagerConfData
│   └── GetOrganizationConfData
├── dao/config/
│   ├── ProvinceCityMapper, LogicAreaConfigMapper
│   ├── OrganizationConfigMapper, ManagerConfigMapper
│   ├── PhysicalAddrConfigMapper, SecurityModelsMapper
│   ├── SecurityRulesMapper, SuspiciousGroupMapper
│   ├── SuspiciousListMapper, WarnStrategyConfMapper
│   ├── WarnStrategyDetailMapper, DataReportingConfMapper
│   ├── DataReportingLogMapper, CollectDataTypeMapper
│   ├── DataFieldMapper, DataCleanupConfMapper
│   └── QuartzJobMapper
└── model/config/
    ├── ProvinceCity, LogicAreaConfig, OrganizationConfig
    ├── ManagerConfig, PhysicalAddrConfig, SecurityModels
    ├── SecurityRules, SuspiciousGroup, SuspiciousList
    ├── WarnStrategyConf, WarnStrategyDetail
    ├── DataReportingConf, DataReportingLog, CollectDataType
    ├── DataField, DataCleanupConf, PointRecord
    └── QuartzJob
```
