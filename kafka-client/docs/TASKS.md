# TASKS.md — kafka-client 局部任务书

## 阶段 1: Kafka 消费框架

### 任务 1.1: 消费者配置
- [ ] `KafkaConsumerConfig` 6 个消费者 Bean 配置
- [ ] SASL_PLAINTEXT 认证（admin/Ankki_Kafka123）
- [ ] 手动提交偏移量（enableAutoCommit=false）
- [ ] maxPollRecords=500, autoOffsetReset=earliest
- [ ] sessionTimeout=15000ms

### 任务 1.2: 消息监听分发
- [ ] `ConsumerListener` 监听入口
- [ ] multipleMsg() — auditContainer 消息处理
- [ ] processApiAuditLog() — apiAuditContainer 消息处理
- [ ] multipleMsgTwo() — dbStaticContainer 消息处理
- [ ] listeningClassifiedMsg() — classifiedContainer 消息处理
- [ ] listeningDmMsg() — dmContainer 消息处理
- [ ] listeningAumdq() — aumdq 消息处理
- [ ] KafkaUtils.manuallySubmitOffset() 偏移量提交

## 阶段 2: 策略解析模式

### 任务 2.1: Audit Log 解析
- [ ] `DataAnalysisStrategy` 策略持有者
- [ ] `JsonDataAnalysis` — JSON 对象/数组解析
- [ ] `ListDataAnalysis` — JSON 列表解析
- [ ] 格式自动检测（开头字符判断）

### 任务 2.2: API Audit 解析
- [ ] `ApiDataAnalysisStrategy` 策略持有者
- [ ] `ApiV1JsonAnalysis` — API v1.0 JSON 解析
- [ ] `ApiV2JsonAnalysis` — API v2.0 占位符
- [ ] DbConfCache 自动注册数据库资产

### 任务 2.3: Anti-Unified Query 解析
- [ ] `DataPAnalysisStrategy` 策略持有者
- [ ] `JsonDataPAnalysis` — JSON 对象/数组解析

## 阶段 3: 数据处理逻辑

### 任务 3.1: Audit Log 处理
- [ ] `ProcessAuditRecord` Runnable
- [ ] 许可证校验
- [ ] 策略解析 → 敏感数据标记 → 格式转换 → ES 写入
- [ ] 许可证容量检查

### 任务 3.2: API Audit 处理
- [ ] `ProcessApiAuditRecord` Runnable
- [ ] 与 ProcessAuditRecord 类似但使用 API 策略

### 任务 3.3: Other Record 处理
- [ ] `ProcessOtherRecord` Runnable
- [ ] processDbAssetData() — aas-db-info 资产处理
- [ ] processDbAssetFlow() — aas-flow-info 流量处理
- [ ] processSensTypeData() — aas-sens-type 敏感类型
- [ ] processDevAlaraInfo() — aas-dev-alarm 告警处理

### 任务 3.4: Classified Record 处理
- [ ] `ProcessClassifiedRecord` Runnable
- [ ] processClassifiedList() — 分类分级数据
- [ ] 8+ 张表写入
- [ ] Redis 敏感类型缓存更新

### 任务 3.5: Data Masking Record 处理
- [ ] `ProcessDmRecord` Runnable
- [ ] processDbAssetData() — sdm-database-config
- [ ] processDmTaskData() — sdm-datamask-db-task
- [ ] processDmFileTaskData() — sdm-datamask-file-task

### 任务 3.6: Anti-Unified Query 处理
- [ ] `AntiUnifiedQueryRecord` Runnable
- [ ] 与 ProcessAuditRecord 类似但使用 DataPAnalysisStrategy

## 阶段 4: 存储写入

### 任务 4.1: ES 写入
- [ ] `DataInToEs` — aassimp-auditlog 写入
- [ ] `DataInPToEs` — aassimp-auditlog (特殊格式)
- [ ] BulkRequest 批量写入
- [ ] 许可证容量检查与等待

### 任务 4.2: MySQL 写入
- [ ] `DataInToMysql` — db_asset_flow_session 批量写入
- [ ] 21 个 MyBatis Mapper 实现
- [ ] Mapper XML 配置

### 任务 4.3: 缓存
- [ ] `DbConfCache` 数据库资产缓存
- [ ] ConcurrentHashMap 存储，Key: dbIp#dbPort#dbName

## 阶段 5: 线程池与调度

### 任务 5.1: 线程池配置
- [ ] `AuditRecordThreadPool` (2/3/30000/8)
- [ ] `ApiAuditRecordThreadPool` (2/3/30000/8)
- [ ] `OtherRecordThreadPool` (3/4/30000/8)
- [ ] `ClassifiedRecordThreadPool` (2/3/30000/8)
- [ ] `DmRecordThreadPool` (2/3/30000/8)
- [ ] `AntiUnifiedQueryThreadPool` (2/3/30000/8)
- [ ] CallerRunsPolicy 拒绝策略

### 任务 5.2: 定时任务
- [ ] `SaticScheduleTask` — 每日 2:00 清理 Kafka 已消费记录

## 阶段 6: 许可证机制

### 任务 6.1: 许可证验证
- [ ] `LicenseUtils.getLicense()` — 调用 checklic 脚本
- [ ] `CodeCache` 许可证缓存
- [ ] LicenseInfo 模型

### 任务 6.2: 许可证监控
- [ ] `KafkaClientWatcher` — ZK /data/licenseUpdateTime Watch
- [ ] 过期消息拦截

## 阶段 7: 敏感数据匹配

- [ ] `SensDataMatchHandler` — Redis 敏感类型匹配
- [ ] `RedisService.hmset()` — 更新 Redis 缓存
- [ ] 小时统计/总计统计更新

## 阶段 8: 异常处理

- [ ] `ExceptionHandleUtils` — 异常日志记录到 jsonExceptionDataDir
- [ ] sys_log 表记录
- [ ] Socket 通知发送

## 阶段 9: 服务集成

- [ ] `KafkaClientApplication` 启动
- [ ] Zookeeper 服务注册
- [ ] Netty ES 冲突修复
- [ ] ProGuard 混淆打包
- [ ] AppAssembler + tar.gz 打包
