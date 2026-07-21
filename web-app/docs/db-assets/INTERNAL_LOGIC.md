# INTERNAL_LOGIC.md — db-assets 数据库资产模块

## 核心流程

### 资产发现流程
```
创建发现任务 → DbTaskDiscoveryService
                    ↓
        Quartz调度(每10分钟) → DbTaskDiscoveryThread
                    ↓
        连接目标数据库 → DatabaseFactory创建对应适配器
                    ↓
        获取库/表/字段元数据 → 入库
                    ↓
        更新发现结果 → DbTaskResult
```

### 资产盘点流程
```
创建盘点任务 → DbInventoryService
                    ↓
        Quartz调度 → DbInventoryThread
                    ↓
        逐库盘点 → 统计表数量、记录数、字段数
                    ↓
        盘点结果入库 → DbInventoryResult
```

### 分类定级流程
```
配置分类规则 → CfgSensClass/Domain/Type/Level
                    ↓
        扫描资产数据 → 匹配分类规则
                    ↓
        生成定级结果 → 定级标签入库
```

### 多数据库适配
- 入口：`DatabaseFactory` 根据数据库类型创建适配器
- 适配器：MysqlDatabase, OracleDatabase, PostgreDatabase, DmDatabase, SqlServerDatabase, HiveDatabase, HbaseDatabase, MongoDbDatabase, HighGoDatabase, Db2Database 等
- 每种适配器实现统一的元数据获取接口
