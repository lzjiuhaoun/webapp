# INTERNAL_LOGIC.md — device 安全设备模块

## 核心流程

### 设备注册与心跳
```
添加设备 → SafetyDevConfigService
                ↓
      设备配置入库 → SafetyDevConfigMapper
                ↓
      Quartz心跳任务 → DevHeartbeatMisson
                ↓
      检测在线/离线状态 → 更新设备状态
                ↓
      离线告警 → 通知 system 模块
```

### DM脱敏设备任务
```
接收脱敏策略 → DmDevService
                ↓
      创建脱敏任务 → DmDbTask / DmFileTask
                ↓
      通过 instruction 模块下发指令
                ↓
      收集脱敏结果 → DmDesenFieldResult / DmDesenTableResult
```

### CM采集设备数据
```
设备心跳检测 → 采集设备数据
                ↓
      CmCollectionData → 入库
                ↓
      发布Kafka事件 → 下游消费(risk, situation)
```

### VS防火墙设备
```
接收审计策略 → VsDevService
                ↓
      创建VS任务 → VsTask
                ↓
      收集拦截数据 → VsCollectionData
```

## 设备类型分类
- CM：态势数据采集设备
- DM：数据库脱敏设备
- VS：数据库防火墙设备
- 通用：其他安全设备
