# CLAUDE.md — support 支撑模块

## 模块使命

提供系统支撑能力：操作日志、系统日志、审计日志搜索、拓扑图展示、WebSocket实时推送和ITA任务管理。

## 技术栈

- MyBatis + MySQL
- Spring Data Elasticsearch（日志搜索）
- Spring WebSocket
- Socket 通信（socket-comm）

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
├── controller/log/
│   └── LogController                ← 日志查询
├── controller/search/
│   ├── SearchController             ← 审计日志搜索
│   └── OemSearchController          ← OEM搜索
├── controller/topology/
│   └── TopGraphController           ← /top (拓扑图)
├── controller/ita/
│   └── ItaTaskController            ← ITA任务
├── controller/monitor/
│   └── ScreenMonitorController      ← 大屏监控
├── controller/open/
│   └── OpenController               ← 开放API
├── websocket/
│   ├── WebSocketChat.java           ← WebSocket端点
│   └── WebSocketConfig.java         ← WebSocket配置
├── service/
│   ├── LogService/Impl + LogThread
│   ├── TopGraphService/Impl
│   ├── ItaTaskService/Impl
│   ├── SearchConditionService/Impl
│   ├── SafeDevRedisCache, RedisService
│   ├── EhCacheInit
│   ├── OfflineAnalysis              ← 离线分析(@Scheduled)
│   └── RealTimeAnalytic             ← 实时分析(@Scheduled)
├── dao/log + search + topology + ita/
│   ├── OperLogMapper, SysLogMapper
│   ├── SqlOperationConfigMapper
│   ├── TopologyMapper
│   └── ItaTaskMapper
└── model/log + search + topology + ita + oem/
    ├── OperLog, SysLog, AuditEventVo
    ├── AuditRecord, AuditRecordCondition, SqlOperationConfig
    ├── NodeData, LinkData, Root, TopFormModel
    ├── ItaTask, TaskCallbackRequest
    ├── EngineStatusInfo
    └── Oem模型
```
