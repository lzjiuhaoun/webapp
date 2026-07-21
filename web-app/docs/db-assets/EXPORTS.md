# EXPORTS.md — db-assets 数据库资产模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/database-assets` | GET/POST/PUT/DELETE | 数据库资产CRUD |
| `/assets-inventory` | GET/POST | 资产盘点任务与结果 |
| `/database-directory` | GET/POST | 数据库目录管理 |
| `/dbasset-classify` | GET/POST/PUT/DELETE | 分类定级管理 |
| `/assets-judge` | GET/POST | 资产研判 |
| `/database-discover` | GET/POST | 资产发现任务 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `DbAssetsService` | 数据库资产增删改查 | risk, situation, report |
| `DbInventoryService` | 资产盘点任务管理 | situation, report |
| `DbClassificationService` | 分类定级管理 | sensdata, strategy |
| `DbTaskDiscoveryService` | 资产发现任务管理 | system |
| `DbJudgeService` | 资产研判 | risk |
| `DbDirectoryService` | 数据库目录管理 | config |
| `DbConfUpdateService` | 数据库配置变更检测 | system |

## 数据模型

| 实体 | 说明 |
|------|------|
| DatabaseInfo | 数据库资产基本信息 |
| DatabaseType | 数据库类型 |
| DbConf | 数据库配置 |
| DbInventory | 盘点任务 |
| DbInventoryResult | 盘点结果 |
| DbTaskDiscovery | 发现任务 |
| DbTaskResult | 发现结果 |
| DbJudge | 研判结果 |
| CfgSensClass/Type/Domain/Level | 分类定级配置 |
