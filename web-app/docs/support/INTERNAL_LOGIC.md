# INTERNAL_LOGIC.md — support 支撑模块

## 核心流程

### 日志管理
- `LogThread` 后台线程收集日志
- 操作日志记录用户操作行为
- 系统日志记录系统事件
- 审计日志存储到 Elasticsearch

### 拓扑图生成
- `TopGraphService` 聚合设备、数据库、网络关系
- 生成节点(NodeData)和边(LinkData)数据
- 支持数据库拓扑、网络拓扑

### WebSocket 实时推送
- `WebSocketChat` 端点处理实时连接
- `WebSocketConfig` 注册 WebSocket 处理器
- 支持前端实时告警推送

### 缓存管理
- `EhCacheInit` 初始化 EhCache
- `RedisService` 提供 Redis 缓存（如配置）
- `SafeDevRedisCache` 设备状态缓存

### 定时分析
- `OfflineAnalysis` — 离线数据定期分析
- `RealTimeAnalytic` — 实时数据分析
