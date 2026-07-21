# EXPORTS.md — backup 备份模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/backup-policy` | GET/POST/PUT/DELETE | 备份策略管理 |
| `/backup-history` | GET/POST | 备份历史查询与恢复 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `BackupPolicyService` | 备份策略管理 | system |
| `BackupHistoryService` | 备份历史管理 | system |
| `BackupQuartzManager` | Quartz任务管理 | SystemStartService |
| `MysqlBackupManagerService` | MySQL备份执行 | BackupPolicyService |

## 数据模型

| 实体 | 说明 |
|------|------|
| BackupPolicy | 备份策略 |
| BackupHistory | 备份历史 |
