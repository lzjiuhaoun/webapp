# EXPORTS.md — support 支撑模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/log` | GET | 操作日志/系统日志查询 |
| `/search` | GET/POST | 审计日志搜索（基于ES）|
| `/search/oem` | GET/POST | OEM日志搜索 |
| `/top` | GET/POST | 拓扑图数据 |
| `/ita-task` | GET/POST/PUT/DELETE | ITA任务管理 |
| `/monitor` | GET | 大屏监控数据 |
| `/open` | GET | 开放API |

## WebSocket

| 端点 | 描述 |
|------|------|
| `/ws/chat` | 实时消息推送 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `LogService` | 日志管理 | 全模块 |
| `TopGraphService` | 拓扑图数据 | situation |
| `ItaTaskService` | ITA任务管理 | system |
| `SearchConditionService` | 搜索条件管理 | audit |
| `OfflineAnalysis` | 离线分析 | situation |
| `RealTimeAnalytic` | 实时分析 | situation |
| `RedisService` | Redis缓存 | 全模块 |
| `SafeDevRedisCache` | 设备缓存 | device |

## 数据模型

| 实体 | 说明 |
|------|------|
| OperLog | 操作日志 |
| SysLog | 系统日志 |
| AuditRecord | 审计记录 |
| SqlOperationConfig | SQL操作配置 |
| NodeData | 拓扑节点 |
| LinkData | 拓扑连接 |
| ItaTask | ITA任务 |
