# EXPORTS.md — manager-center 出口契约

> 版本: 1.0.0
> 维护: manager-center 模块负责人

## 一、Socket 消息接收服务

| 端点 | 协议 | 说明 |
|------|------|------|
| `localhost:9090` | TCP Socket | 消息接收和路由转发 |

## 二、消息格式

### 管道分隔格式

所有通过 manager-center 收发的消息必须遵循以下格式：

```
messageType|contentType|operType|operNum|uuids|timestampMill
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `messageType` | int | 消息类型（用于路由匹配） |
| `contentType` | int | 内容类型（用于路由匹配） |
| `operType` | int | 操作类型 |
| `operNum` | int | 操作编号 |
| `uuids` | String | 消息 UUID |
| `timestampMill` | long | 时间戳（毫秒） |

**路由规则**：仅 `messageType` 和 `contentType` 用于路由决策，其余字段原样转发。

### 状态码

| 状态码 | 说明 |
|--------|------|
| `0` | 消息接收成功 |
| `1` | Socket 连接失败 |
| `2` | 验证失败（IP 非 127.0.0.1） |

## 三、特殊消息

| messageType | contentType | 行为 |
|-------------|------------|------|
| 4 | 1 | 执行系统重启脚本 `cd /home/simp/conf/client; sh start_all_service.sh` |

该消息**不查询路由表**，不转发，仅本地执行。

## 四、MySQL 路由表

### 表名: `message_routing`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Integer | 主键 |
| `message_type` | Byte | 消息类型（路由匹配） |
| `content_type` | Byte | 内容类型（路由匹配） |
| `module_serversocket_host` | String | 目标主机地址 |
| `module_serversocket_port` | Integer | 目标端口号 |
| `extend_a` | String | 扩展字段 A |
| `extend_b` | String | 扩展字段 B |
| `extend_c` | String | 扩展字段 C |

### 路由查询

```sql
SELECT * FROM message_routing
WHERE message_type = ? AND content_type = ?
```

**路由策略**：
- 0 条匹配 → 消息丢弃，记录日志
- 1 条匹配 → 转发到目标 host:port
- N 条匹配 → **广播**到所有目标 host:port
- 每条连接独立：connect → send → close

### 示例路由条目

| messageType | contentType | module_serversocket_host | module_serversocket_port |
|-------------|------------|-------------------------|--------------------------|
| 1 | 1 | 192.168.1.10 | 8080 |
| 1 | 1 | 192.168.1.11 | 8080 |
| 2 | 1 | 192.168.1.20 | 9090 |

## 五、MyBatis Mapper 接口

| 方法 | 用途 |
|------|------|
| `insert(MessageRouting)` | 新增路由规则 |
| `insertSelective(MessageRouting)` | 选择性新增 |
| `updateByPrimaryKey(MessageRouting)` | 全量更新 |
| `updateByPrimaryKeySelective(MessageRouting)` | 选择性更新 |
| `deleteByPrimaryKey(Integer)` | 删除路由规则 |
| `selectByPrimaryKey(Integer)` | 按 ID 查询 |
| `selectObjByParams(Integer messageType, Integer contentType)` | 按路由参数查询（核心） |

## 六、服务注册

- Zookeeper 路径: `/services/managercenter`
- 注册数据: `localhost:9090`
- 节点类型: `EPHEMERAL`（进程退出自动删除）

## 七、线程池配置

| 参数 | 值 | 说明 |
|------|-----|------|
| Core Size | 6 | 固定线程数 |
| Max Size | 6 | 无溢出 |
| Keep Alive | 1800s | 空闲线程存活时间 |
| Queue Size | 50 | ArrayBlockingQueue 容量 |
| Rejection | CallerRunsPolicy | 调用线程执行 |
