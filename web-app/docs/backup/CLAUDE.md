# CLAUDE.md — backup 备份模块

## 模块使命

管理系统配置和数据的备份与恢复：全量备份、增量备份、备份策略管理和备份历史查询。

## 技术栈

- MyBatis + MySQL
- Quartz 定时任务（定时备份）
- MySQL mysqldump

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
├── controller/backup/
│   ├── BackupHistoryController      ← 备份历史
│   └── BackupPolicyController       ← 备份策略
├── service/
│   ├── BackupHistoryService/Impl
│   ├── BackupPolicyService/Impl
│   ├── BackupQuartzManager          ← 备份Quartz管理
│   └── MysqlBackupManagerService    ← MySQL备份执行
├── dao/backup/
│   ├── BackupHistoryMapper
│   └── BackupPolicyMapper
└── job/
    └── DataBackupTaskJob.java       ← Quartz Job实现
```
