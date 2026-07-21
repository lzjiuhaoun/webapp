# CLAUDE.md — db-assets 数据库资产模块

## 模块使命

管理数据库资产全生命周期：资产注册、发现、盘点、分类定级、研判、变更日志等。

## 技术栈

- MyBatis + MySQL
- Quartz 定时任务
- 多数据库适配（DatabaseFactory + 各数据库实现）
- Elasticsearch（部分检索场景）

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
├── controller/dbassets/
│   ├── DbAssetsController         ← /database-assets
│   ├── DbInventoryController      ← /assets-inventory
│   ├── DbDirectoryController      ← /database-directory
│   ├── DbClassificationController ← /dbasset-classify
│   ├── DbJudgeController          ← /assets-judge
│   └── DbTaskDiscoveryController  ← /database-discover
├── service/
│   ├── DbAssetsService/Impl
│   ├── DbInventoryService/Impl + DbInventoryThread/DbInventoryDiscoveryThread
│   ├── DbDirectoryService/Impl
│   ├── DbClassificationService/Impl
│   ├── DbJudgeService/Impl + DbJudgeCheckThread
│   ├── DbTaskDiscoveryService/Impl + DbTaskDiscoveryThread
│   ├── DbConfUpdateService/Impl
│   ├── GetDbChangeLog, GetDbConfData, GetDbTaskResultsData
│   ├── PretreatmentThread, Transform, DbStatisticsThread, MongoDbTranfom
│   └── DbConfDevInfo, Task
├── dao/dbassets/
│   ├── DatabaseInfoMapper, DatabaseTypeMapper
│   ├── DbConfMapper, DbConfExtendMapper, DbConfUpdateHistoryMapper
│   ├── DbInventoryMapper, DbInventoryResultMapper, DbInventoryDevMapper
│   ├── DbTaskDiscoveryMapper, DbTaskResultMapper
│   ├── DbJudgeMapper, DbStatisticsInfoMapper, ChangeLogMapper, PretreatmentResultMapper
│   └── CfgSens*Mapper (分类定级配置)
└── model/dbassets/
    ├── DatabaseInfo, DatabaseType, DbConf, DbConfExtend, DbConfHistoryStat
    ├── DbInventory, DbInventoryResult, DbInventoryDev
    ├── DbTaskDiscovery, DbTaskResult
    ├── DbJudge, DbStatisticsInfo, ChangeLog
    ├── CfgSensClass/Type/Domain/Level
    └── DTO/VO classes
```
