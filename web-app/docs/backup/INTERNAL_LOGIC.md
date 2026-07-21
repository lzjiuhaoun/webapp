# INTERNAL_LOGIC.md — backup 备份模块

## 核心流程

### 备份执行
```
定时触发(Quartz) → DataBackupTaskJob
                       ↓
         BackupQuartzManager → MysqlBackupManagerService
                       ↓
         全量备份 → mysqldump 全库
         增量备份 → binlog 增量
                       ↓
         备份文件存储 → 本地/远程路径
                       ↓
         记录备份历史 → BackupHistory
```

### 备份策略
- 全量备份：每天凌晨2点（`0 0 2 * * ?`）
- 增量备份：每6小时（`0 0 */6 * * ?`）
- 可配置保留天数和备份路径

### 数据恢复
- 选择备份历史记录
- 执行恢复脚本
- 验证恢复结果
