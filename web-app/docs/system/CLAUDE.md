# CLAUDE.md — system 系统管理模块

## 模块使命

管理系统运行信息、告警、维护、升级、License校验、安全配置和主题设置，保障系统正常运维。

## 技术栈

- Spring Boot
- Quartz 定时任务
- MyBatis + MySQL
- Socket 通信（向 manager-center 交互）

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
├── controller/system/
│   ├── SystemInfoController         ← /system-info
│   ├── SystemMaintenanceController  ← /systemMaintenance
│   ├── SystemUpgradeController      ← /system-upgrade
│   ├── SystemTimeController         ← /system-time
│   ├── SystemThemeController        ← /system-theme
│   ├── SystemAlarmController        ← /system-alarm
│   ├── SecurityConfigController     ← /system-safety
│   ├── LoginIpConfigController      ← /system-safety
│   ├── LicenseController            ← /license
│   └── DeployController             ← /manager-port
├── service/
│   ├── SystemInfoService/Impl
│   ├── SystemMaintenanceService/Impl
│   ├── SystemUpgradeService/Impl
│   ├── SystemTimeService/Impl
│   ├── SystemThemeService/Impl
│   ├── SystemAlarmService/Impl
│   ├── SecurityConfigService/Impl
│   ├── LoginIpConfigService/Impl
│   ├── LicenseService/Impl
│   ├── NtpConfigService/Impl
│   ├── DeploymentService/Impl
│   ├── SystemStartService           ← 应用启动，初始化Quartz
│   ├── SystemMaintenanceThread
│   ├── SystemOperation
│   ├── DataCleanupThread
│   ├── FactoryDataReset
│   └── RandomValidateCodeService
├── dao/system/
│   ├── SysConfMapper, SystemInfoMapper, SystemAlarmMapper
│   ├── LoginIpConfigMapper, NtpConfigureMapper
│   ├── SystemSecurityConfigMapper, SysThemeMapper
│   ├── AlarmModeMapper, LogInfoCollectionMapper
│   └── QuartzJobMapper
└── model/system/
    ├── SystemInfo, SysConf, SystemAlarm, AlarmInfo, AlarmMode
    ├── LoginIpConfig, NtpConfigure, SystemSecurityConfig
    ├── SysTheme, LicenseInfo, UpgradeVersionInfo
    ├── ManagePortInfo, OperationLog, LogInfoCollection
    └── QuartzJob
```
