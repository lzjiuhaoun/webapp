# TASKS.md — common-module 局部任务书

## 开发任务

### 阶段 1: 基础工具类
- [ ] SM4Util 国密加密工具实现
- [ ] LinuxUtils Linux 命令执行工具
- [ ] DatabaseUrlParserUtil 数据库 URL 解析
- [ ] CommonBeanUtil Bean 工具与 EnumSingleton

### 阶段 2: Zookeeper 集成
- [ ] ZookeeperClient 连接管理
- [ ] 节点 CRUD 操作封装
- [ ] Watcher 机制与 WatcherProtector
- [ ] 服务注册/发现接口

### 阶段 3: Redis 集成
- [ ] RedisConfig 配置类
- [ ] RedisUtil 工具类
- [ ] Jackson 序列化配置

### 阶段 4: 数据库备份
- [ ] AbstractMysqlBackupManager 抽象类
- [ ] DatasourceBackup 管理器
- [ ] BackupPolicy/BackupHistory 模型
- [ ] 全量/增量备份策略
- [ ] SM4 加密备份文件
