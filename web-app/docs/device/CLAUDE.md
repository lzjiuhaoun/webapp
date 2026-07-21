# CLAUDE.md — device 安全设备模块

## 模块使命

管理接入的安全设备：CM态势采集、DM数据库脱敏、VS数据库防火墙、通用安全设备。包括设备注册、配置管理、心跳检测、告警采集和任务下发。

## 技术栈

- MyBatis + MySQL
- Quartz 定时任务（设备心跳检测）
- Socket 通信（socket-comm）
- Kafka（事件订阅）

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
├── controller/devconf/
│   ├── CmDevController              ← /cm (态势采集设备)
│   ├── DmDevController              ← /dm (脱敏设备)
│   ├── VsDevController              ← /vs (数据库防火墙)
│   ├── SafetyDevConfigController    ← /safety-devices (通用设备)
│   └── DevAlarmHistoryController    ← /dev-alarm (设备告警)
├── service/
│   ├── CmDevService/Impl
│   ├── DmDevService/Impl
│   ├── VsDevService/Impl
│   ├── SafetyDevConfigService/Impl
│   ├── DevExtendConfigService/Impl
│   ├── DevAlarmHistoryService/Impl
│   ├── OemConfigService/Impl
│   ├── SimpSysMonitorService/Impl
│   └── KafkaAclThread
├── dao/devconf/
│   ├── SafetyDevConfigMapper, DevTypeMapper, DevExtendConfigMapper
│   ├── DevAlarmHistoryMapper, DevDataTypeMapper
│   ├── DmDbTaskMapper, DmDesenFieldResultMapper, DmDesenTableResultMapper
│   ├── DmFileTaskMapper, VsTaskMapper, TopicRelationInfoMapper
│   └── VulnerabilityMapper
└── model/devconf/
    ├── SafetyDevConfig, DevType, DevExtendConfig, DevAlarmHistory
    ├── DmDbTask, DmDesenFieldResult, DmDesenTableResult, DmFileTask
    ├── VsTask, CmCollectionData, VsCollectionData
    ├── Task, TaskRecord, TaskVaResult, TaskWpResult
    ├── Vulnerability, TopicRelationInfo, DevDbConf
    └── SafeDevDto, SafetyDevStrategyConfig, IpArrayVO, etc.
```
