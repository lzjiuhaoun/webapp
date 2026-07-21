# GLOBAL_CONTRACTS.md — 规范条约

## 一、全局唯一标识符规范

| 标识符类型 | 格式 | 示例 | 使用范围 |
|-----------|------|------|---------|
| **服务名** | 小写驼峰 | `syslogserver`, `kafkaclient`, `webapp` | Zookeeper 注册、Socket 路由 |
| **Kafka Topic** | `aas-` 前缀 + 连字符 | `aas-audit-log`, `aas-db-info` | collection-engine → kafka-client |
| **ES 索引** | `aassimp-` 前缀 + 连字符 | `aassimp-auditlog`, `aassimp-apiauditlog` | kafka-client → report-engine |
| **Socket 消息 UUID** | 标准 UUID | `550e8400-e29b-41d4-a716-446655440000` | 进程间消息追踪 |

## 二、Socket 通信协议

### 消息格式

所有通过 manager-center 转发的 Socket 消息必须遵循以下管道分隔格式：

```
messType|contentType|operType|operNum|uuids|timestamp
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `messType` | int | 消息类型（0=普通，4=系统重启指令等） |
| `contentType` | int | 内容类型 |
| `operType` | int | 操作类型 |
| `operNum` | int | 操作编号 |
| `uuids` | String | 消息 UUID |
| `timestamp` | long | 时间戳 |

### 特殊消息

- **类型 4，内容 1**：触发全系统重启，执行 `/home/simp/conf/client/start_all_service.sh`
- 该消息由 `ManagementCenterMessHandler` 在 manager-center 中特殊处理

### 端口约定

| 模块 | 端口 | 角色 |
|------|------|------|
| `manager-center` | 9090 | TCP Server（所有模块的消息中转） |
| `web-app` | 9095 | TCP Server（接收 manager-center 转发） |
| `collection-engine` | 动态 | TCP Server（接收 manager-center 转发） |
| 其余模块 | 动态 | 按需启动 TCP Server |

## 三、模块间边界承诺

### collection-engine → kafka-client

- **承诺**：collection-engine 按数据分类将消息生产到对应 Kafka Topic
- **保证**：消息体为可解析的 JSON 或约定格式的字符串
- **Topic 映射**：
  - `aas-audit-log` — 审计日志（风险/非风险）
  - `aas-db-info` — 资产配置
  - `aas-flow-info` — 资产流量
  - `aas-sens-type` — 分类敏感类型
  - `aas-dev-alarm` — 设备告警

### kafka-client → report-engine

- **承诺**：kafka-client 将处理后的数据写入 ES 和 MySQL
- **保证**：ES 索引结构稳定，MySQL 表结构变更需同步更新 report-engine 的 Mapper

### manager-center → 所有模块

- **承诺**：manager-center 根据 `message_routing` 表路由消息
- **保证**：路由表中记录目标模块的 host:port，模块需确保 Socket Server 可用
- **路由表结构**：`message_routing` 表，按消息类型/内容类型匹配目标

### daemon → 所有模块

- **承诺**：daemon 通过 Zookeeper 监控服务健康
- **保证**：各进程启动时注册到 `/services/{serviceName}`，退出时移除节点
- **监控服务列表**：`reportengine,syslogserver,webapp,managercenter,kafkaclient,kafkaclient1~5`

### common-module → 所有模块

- **承诺**：提供 Zookeeper 客户端封装、Redis 配置、SM4 加密工具、数据库备份工具
- **保证**：接口稳定，向后兼容

### socket-comm → 所有依赖模块

- **承诺**：提供 TCP Socket 客户端/服务端、消息队列、默认消息处理器
- **保证**：`MessageToServerSocket` 静态方法可发送消息到 manager-center

## 四、数据分类编码

collection-engine 使用的 `dataType` 分类码，kafka-client 必须遵循：

| dataType | 含义 | Kafka Topic |
|----------|------|-------------|
| 0 | 非风险审计日志 | `aas-audit-log` |
| 1 | 风险审计日志 | `aas-audit-log` |
| 2 | 资产配置 | `aas-db-info` |
| 3 | 资产流量 | `aas-flow-info` |
| 9 | 设备告警 | `aas-dev-alarm` |

## 五、Kafka 消费者组约定

| Topic | Consumer Group | 说明 |
|-------|---------------|------|
| `aas-audit-log` | `auditContainer` | 审计日志消费 |
| `aas-db-info` | `dbStaticContainer` | 资产信息消费 |
| `aas-sens-type` | `classifiedContainer` | 分类敏感消费 |
| `sdm-*` | `dmContainer` | 脱敏任务消费 |
| `aas-api-audit-log` | `apiAuditContainer` | API 审计消费 |
