# CLAUDE.md — sensdata 敏感数据模块

## 模块使命

管理敏感数据的识别、发现、统计和字典管理。支持多种敏感类型（身份证、手机号、银行卡等35+种），自动发现和扫描敏感数据。

## 技术栈

- MyBatis + MySQL
- Quartz 定时任务（敏感数据发现）
- Spring Data Elasticsearch（敏感数据检索）
- dm-engine 脱敏引擎

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
├── controller/sensdata/
│   ├── SensitiveTypeController      ← 敏感类型管理
│   ├── SensDataTaskController       ← 敏感数据发现任务
│   ├── SensDataSummaryController    ← 敏感数据统计汇总
│   └── DictionaryController         ← 字典管理
├── service/
│   ├── SensitiveService/Impl
│   ├── SensDataFindService/Impl
│   ├── SensDataSummaryService/Impl
│   ├── DictionaryService/Impl
│   ├── AutoDiscovery
│   ├── GetSensResultsSumData
│   ├── SensDataJobThread
│   └── ScheduledUpdateDictionaryThread
├── dao/sensdata/
│   ├── SensTypeMapper, SensTypeDetailMapper
│   ├── FindTaskMapper, FindRecordMapper
│   ├── FindInfoMapper, FindDetailsMapper, FindDetailsSummaryMapper
│   ├── DataDictionaryMapper, DictionaryMapper
└── model/sensdata/
    ├── SensType, SensTypeDetail
    ├── FindTask, FindRecord, FindInfo
    ├── FindDetails, FindDetailsSum
    ├── DataDictionary, DataDictionaryDetail
    └── Cell
```
