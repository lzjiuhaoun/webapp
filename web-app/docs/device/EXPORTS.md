# EXPORTS.md — device 安全设备模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/cm` | GET/POST/PUT/DELETE | CM态势采集设备管理 |
| `/dm` | GET/POST/PUT/DELETE | DM脱敏设备管理 |
| `/vs` | GET/POST/PUT/DELETE | VS数据库防火墙管理 |
| `/safety-devices` | GET/POST/PUT/DELETE | 通用安全设备管理 |
| `/dev-alarm` | GET | 设备告警历史查询 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `SafetyDevConfigService` | 安全设备配置管理 | system, risk, instruction |
| `CmDevService` | CM设备数据采集 | situation, risk |
| `DmDevService` | DM脱敏设备管理 | strategy, sensdata |
| `VsDevService` | VS防火墙设备管理 | risk, strategy |
| `DevAlarmHistoryService` | 设备告警历史 | system, risk |
| `DevExtendConfigService` | 设备扩展配置 | strategy |

## 数据模型

| 实体 | 说明 |
|------|------|
| SafetyDevConfig | 安全设备配置 |
| DevType | 设备类型 |
| DevExtendConfig | 设备扩展配置 |
| DevAlarmHistory | 设备告警历史 |
| DmDbTask/DmFileTask | DM脱敏任务 |
| DmDesenFieldResult/DmDesenTableResult | DM脱敏结果 |
| VsTask | VS任务 |
| Vulnerability | 漏洞信息 |
