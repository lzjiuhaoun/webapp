# INTERNAL_LOGIC.md — system 系统管理模块

## 核心流程

### 系统启动流程
```
ApplicationReady → SystemStartService
                        ↓
              初始化Quartz定时任务
              ├── 数据库统计任务
              ├── 资产盘点任务
              ├── 开放端口检测任务
              ├── 设备心跳检测任务
              ├── NTP同步任务
              └── 重启任务
```

### 告警处理流程
```
风险/设备事件 → SystemAlarmService
                        ↓
              匹配告警模式(AlarmMode)
              ↓
              根据级别通知(通过 server 模块)
              ↓
              记录告警历史
```

### Quartz 任务管理
- 入口：`QuartzManager.java` — 统一调度
- 任务实现：`QuartzJobImpl.java` 委托给具体业务 Service
- 持久化：Quartz JDBC 表（MySQL）
- 关键任务类：`DbInventoryMisson`, `DevHeartbeatMisson`, `SensDataFindMisson` 等

### License 校验
- 启动时读取 License 文件
- 校验有效期、功能模块授权
- 过期时限制未授权功能
