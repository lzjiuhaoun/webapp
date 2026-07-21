# INTERNAL_LOGIC.md — config 基础配置模块

## 核心流程

### 配置数据同步
```
手动配置/定时任务 → Config Service
                       ↓
        数据入库 → Config Mapper
                       ↓
        后台线程同步 → GetManagerConfData / GetOrganizationConfData
                       ↓
        其他模块读取配置
```

### 告警策略匹配
- 告警策略配置包含触发条件、通知方式、告警级别
- system 模块产生告警时，匹配 WarnStrategyConf
- 匹配成功后通过 server 模块发送通知

### 嫌疑列表管理
- 支持IP、域名、URL等类型的嫌疑数据
- 按分组管理，支持批量导入
- risk 和 device 模块在分析时匹配嫌疑列表
