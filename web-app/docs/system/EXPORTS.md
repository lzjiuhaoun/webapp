# EXPORTS.md — system 系统管理模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/system-info` | GET | 获取系统信息 |
| `/systemMaintenance` | POST/PUT | 系统维护操作 |
| `/system-upgrade` | POST/GET | 系统升级管理 |
| `/system-time` | GET/PUT | NTP时间同步 |
| `/system-theme` | GET/PUT | 系统主题配置 |
| `/system-alarm` | GET/POST/PUT/DELETE | 系统告警管理 |
| `/system-safety` | GET/POST/PUT/DELETE | 安全配置、IP白名单 |
| `/license` | GET/POST | License校验与管理 |
| `/manager-port` | GET/PUT | 管理端口配置 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `SystemInfoService` | 系统运行信息获取 | 全模块 |
| `SystemAlarmService` | 系统告警管理 | risk, device |
| `LoginIpConfigService` | IP白名单校验 | auth |
| `LicenseService` | License校验 | 全模块 |
| `SecurityConfigService` | 安全配置管理 | config |
| `NtpConfigService` | NTP同步 | system |
| `SystemStartService` | 应用启动时Quartz初始化 | 内部 |

## 数据模型

| 实体 | 说明 |
|------|------|
| SystemInfo | 系统基本信息 |
| SysConf | 系统配置 |
| SystemAlarm | 系统告警 |
| AlarmMode | 告警模式 |
| LoginIpConfig | IP白名单配置 |
| SystemSecurityConfig | 安全配置 |
| SysTheme | 主题配置 |
| LicenseInfo | License信息 |
| NtpConfigure | NTP配置 |
| QuartzJob | Quartz任务配置 |
