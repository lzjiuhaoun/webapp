# INTEGRATION_TASKS.md — 集成里程碑

## 集成测试点

### M1: 服务注册与发现

| 任务 | 涉及模块 | 集成点 | 验证方式 |
|------|---------|--------|---------|
| 服务启动注册 | 全部进程 | Zookeeper `/services/{name}` | 检查 ZK 节点是否存在 |
| 服务心跳保活 | 全部进程 | Zookeeper 临时节点 | 进程存活时节点不消失 |
| 服务发现 | manager-center | ZK 获取目标模块地址 | 路由表可查询到目标 |

### M2: Socket 消息路由

| 任务 | 涉及模块 | 集成点 | 验证方式 |
|------|---------|--------|---------|
| 消息发送 | 任意模块 → manager-center | Socket TCP 9090 | manager-center 收到消息 |
| 消息转发 | manager-center → 目标模块 | Socket TCP 目标端口 | 目标模块收到消息 |
| 系统重启指令 | 任意模块 → manager-center | 消息类型 4，内容 1 | 执行 `start_all_service.sh` |

### M3: Kafka 数据流

| 任务 | 涉及模块 | 集成点 | 验证方式 |
|------|---------|--------|---------|
| Syslog → Kafka | collection-engine → Kafka | Topic `aas-audit-log` 等 | Kafka 有消息堆积 |
| Kafka → ES/MySQL | Kafka → kafka-client | ES 索引有数据 | ES 查询返回记录 |
| 数据分类正确性 | collection-engine → kafka-client | dataType 映射 | 各 Topic 数据符合预期 |

### M4: 报表统计

| 任务 | 涉及模块 | 集成点 | 验证方式 |
|------|---------|--------|---------|
| ES 聚合查询 | report-engine → ES | `aassimp-auditlog` | 统计数据非空 |
| MySQL 报表写入 | report-engine → MySQL | 报表表有数据 | web-app 可查询报表 |
| 定时任务触发 | report-engine 内部 | Quartz 调度 | 日志显示定时任务执行 |

### M5: Web 端到端

| 任务 | 涉及模块 | 集成点 | 验证方式 |
|------|---------|--------|---------|
| 用户登录 | web-app → CAS | SSO 认证通过 | 获取 token |
| 审计日志检索 | web-app → ES | 检索接口返回数据 | 前端展示日志列表 |
| 风险态势展示 | web-app → MySQL (报表) | 态势接口返回数据 | 前端图表渲染 |
| WebSocket 推送 | web-app → 浏览器 | WebSocket 连接 | 实时消息接收 |

### M6: 守护进程

| 任务 | 涉及模块 | 集成点 | 验证方式 |
|------|---------|--------|---------|
| 服务宕机检测 | daemon → ZK Watch | ZK 节点消失 | daemon 日志记录异常 |
| 服务自动重启 | daemon → 目标进程 | 执行重启脚本 | 目标进程恢复运行 |
