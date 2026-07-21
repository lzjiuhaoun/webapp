# TESTING.md — backup 备份模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 备份策略逻辑 |
| 集成测试 | Spring Boot Test | 备份执行 |

## Mock 边界

- mysqldump → Mock 进程执行
- Quartz → 直接调用
- 文件存储 → Mock 文件系统
