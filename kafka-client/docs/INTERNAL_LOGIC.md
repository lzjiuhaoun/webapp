# INTERNAL_LOGIC.md — kafka-client 内部实现逻辑

> 本模块描述 kafka-client 的内部数据流、处理逻辑和实现细节。

## 一、Kafka 消费总体架构

### 消费者注册

`KafkaConsumerConfig` 配置 6 个消费者 Bean，每个消费者绑定特定的 Topic 和消费者组：

```
auditContainer      → aas-audit-log
dbStaticContainer   → aas-db-info, aas-flow-info, aas-sens-type, aas-dev-alarm
classifiedContainer → aas-classified-list
dmContainer         → sdm-database-config, sdm-datamask-db-task, sdm-datamask-file-task
apiAuditContainer   → aas-api-audit-log
aumdq               → aas-p-log
```

### 消息监听分发

`ConsumerListener` 是消息监听入口，按 Topic 分发到不同处理逻辑：

1. `multipleMsg()` — 消费 auditContainer，路由到 AuditRecordThreadPool → ProcessAuditRecord
2. `processApiAuditLog()` — 消费 apiAuditContainer，路由到 ApiAuditRecordThreadPool → ProcessApiAuditRecord
3. `multipleMsgTwo()` — 消费 dbStaticContainer，路由到 OtherRecordThreadPool → ProcessOtherRecord
4. `listeningClassifiedMsg()` — 消费 classifiedContainer，路由到 ClassifiedRecordThreadPool → ProcessClassifiedRecord
5. `listeningDmMsg()` — 消费 dmContainer，路由到 DmRecordThreadPool → ProcessDmRecord
6. `listeningAumdq()` — 消费 aumdq，路由到 AntiUnifiedQueryThreadPool → AntiUnifiedQueryRecord

### 偏移量管理

所有消费者组手动提交偏移量（`enableAutoCommit=false`），处理完成后通过 `KafkaUtils.manuallySubmitOffset()` 提交，确保消息不丢失。

## 二、Audit Log 处理流程

### ProcessAuditRecord

1. **许可证校验**：检查 `CodeCache.licenseInfo.expiryTime > proStartTime`，过期则跳过消息
2. **策略解析**：调用 `DataAnalysisStrategy.analyze(record.value())`
   - 检查 `record.value().toString().startsWith("{")` 判断格式
   - JSON 对象 → `JsonDataAnalysis`
   - JSON 数组 → `JsonDataAnalysis`（内部处理数组）
3. **敏感数据标记**：`SensDataMatchHandler.sensDataHandle()` 逐个记录匹配 Redis 敏感类型缓存，匹配则 `setSensData(1)`
4. **格式转换**：`RecordSerialize.convertToMap()` 将 `AuditRecord`/`AuditRecordVo` 转为 `LinkedHashMap`
5. **许可证容量检查**：对比 ES 当前文档数与 `100M * dataLogInfo`，超限则等待（10 秒间隔重试）
6. **ES 批量写入**：`DataInToEs.dataToElasticsearch(processCompleteList, "aassimp-auditlog")`
   - 使用 `BulkRequest` 批量写入
   - 文档 ID 使用消息 UUID

### ProcessApiAuditRecord

流程与 ProcessAuditRecord 类似，差异点：
- 使用 `ApiDataAnalysisStrategy` 解析（默认 `ApiV1JsonAnalysis`）
- `ApiV1JsonAnalysis` 在解析时调用 `DbConfCache.getOrCreateAssetFromCache()` 自动注册数据库资产
- 输出到 ES 索引 `aassimp-apiauditlog`

### AntiUnifiedQueryRecord

流程与 ProcessAuditRecord 类似，差异点：
- 使用 `DataPAnalysisStrategy` 解析（默认 `JsonDataPAnalysis`）
- `JsonDataPAnalysis` 支持单条 JSON 和 JSON 数组
- 输出到 ES 索引 `aassimp-auditlog`（与审计日志合并）

## 三、Other Record 处理流程

### ProcessOtherRecord — 按 Topic 路由

1. **aas-db-info** → `processDbAssetData()`
   - 解析 `DbAssetConfig`，提取 `addList`、`deleteList`、`updateList`
   - `updateList` → `DbConfMapper.updateByPrimaryKeySelective()`
   - `deleteList` → `DbConfMapper.deleteByUuId()`
   - `addList` → `DbConfMapper.insertSelective()` + `DbJudgeMapper.insertSelective()`
   - 记录 `change_log` 变更日志

2. **aas-flow-info** → `processDbAssetFlow()`
   - 解析 `DbAssetFlowSession` 列表
   - 调用 `DataInToMysql.dataToMysql()` 批量插入 `db_asset_flow_session` 表

3. **aas-sens-type** → `processSensTypeData()`
   - 解析 `SensType` 和 `SensTypeDetail`
   - `SensTypeMapper.insertOrUpdate()` 或 `insertSelective()`
   - `SensTypeDetailMapper.insert()` 或 `updateByPrimaryKeySelective()`

4. **aas-dev-alarm** → `processDevAlaraInfo()`
   - 解析 `DevAlarmHistory`
   - `DevAlarmHistoryMapper.insertSelective()`

## 四、Classified Record 处理流程

### ProcessClassifiedRecord — processClassifiedList()

1. 解析 `ResultData` 包装的分类结果
2. 遍历分类列表，逐个处理：
   - `ClassifiedMapper.batchInsertList()` → classified 表
   - `DatabaseInfoMapper.insertDatabaseInfo()` → database_info 表
   - `DbStatisticsInfoMapper.insertSelective()` → db_statistics_info 表（统计汇总）
   - `FindTaskMapper.insert()` → find_task 表（发现任务）
   - `FindInfoMapper.insertSelective()` → find_info 表
   - `FindDetailsMapper.insertSelective()` → find_details 表
   - `DbConfExtendMapper.insert()` → db_conf_extend 表（Oracle 用户）
   - `DbConfMapper.updateByUuIdSelective()` → db_conf 表
3. `RedisService.hmset()` 更新 Redis `sens-type` 哈希，供敏感数据标记使用

## 五、Data Masking Record 处理流程

### ProcessDmRecord — 按 Topic 路由

1. **sdm-database-config** → `processDbAssetData()`
   - 与 aas-db-info 相同逻辑更新 `db_conf` 和 `db_judge`

2. **sdm-datamask-db-task** → `processDmTaskData()`
   - 解析 `DmTaskInfo` 包含任务信息和结果
   - `DmDbTaskMapper.insert()` → dm_db_task 表
   - `DmDesenTableResultMapper.insertForeach()` → dm_desen_table_result 表
   - `DmDesenFieldResultMapper.insertForeach()` → dm_desen_field_result 表

3. **sdm-datamask-file-task** → `processDmFileTaskData()`
   - 解析 `DmFileTask`
   - `DmFileTaskMapper.insert()` → dm_file_task 表

## 六、策略模式实现

### DataAnalysisStrategy（审计日志策略持有者）

```
DataAnalysisStrategy.analyze(record)
  → 检测 record.value() 是否以 "{" 开头
    → 是: JsonDataAnalysis (JSON 对象/数组)
    → 否: ListDataAnalysis (JSON 列表)
```

### ApiDataAnalysisStrategy（API 审计策略持有者）

```
ApiDataAnalysisStrategy.analyze(record)
  → 默认: ApiV1JsonAnalysis (k8sApiAuditLogFormatJson)
    → 解析 JSON 为 ApiV1AuditRecord
    → 自动注册数据库资产 (DbConfCache)
```

### JsonDataAnalysis 内部逻辑

1. 检查 `record.value()` 是否为 JSON 数组（`startsWith("[")`）
2. 数组：解析为 `AuditRecord[]`，遍历处理
3. 对象：解析为单个 `AuditRecord` 或 `AuditRecordVo`（防火墙格式）
4. 敏感数据标记 → 序列化 → 许可证检查 → 返回结果列表

### DbConfCache（数据库资产缓存）

- 启动时从 MySQL `db_conf` 表加载所有资产到 `ConcurrentHashMap<String, DbAssetConfig>`
- 键：`dbIp#dbPort#dbName`
- API 审计解析时，按此缓存自动注册新发现的数据库资产

## 七、SensDataMatchHandler（敏感数据匹配）

1. **Redis Key 构造**:
   - `destIp + "#" + destPort + "#" + dbName + "#" + tableName + [fieldName]`
   - 如果 fieldName 为 `*`，使用 `IP#port#dbName#tableName`

2. **Redis 查询**:
   - Key: `sens-type`（哈希表）
   - Hash Key: 上述构造的字符串
   - Hash Value: 逗号分隔的 sensId 列表

3. **标记**:
   - 匹配成功：`auditRecord.setSensData(1)`
   - 更新 Redis 统计：
     - 小时统计: `sens-{timestamp}` 哈希表
     - 总计统计: `sens-visit-number` ZSet

## 八、ES 写入实现

### DataInToEs

1. 从 `ElasticClientConfig` 获取 ES 客户端
2. 检查当前索引文档数是否超过许可证限制
3. 超限则 `Thread.sleep(10000)` 等待，循环重试
4. 构造 `BulkRequest`，逐个添加 `IndexRequest`
5. 执行 `bulk()` 批量写入

### DataInPToEs

与 `DataInToEs` 类似，针对 `aas-p-log` 特殊格式，使用 `JsonDataPAnalysis` 解析后写入。

## 九、许可证验证机制

### 启动流程

1. `KafkaClientApplication.run()` 调用 `ZookeeperClientUtil.getZookeeperClient()`
2. 连接 ZK，读取 `/data/licenseUpdateTime` 节点数据
3. 启动 `KafkaClientWatcher` 监听许可证更新时间变化

### CodeCache.init() 初始化

1. 调用 `LicenseUtils.getLicense()`
2. 执行 Shell 脚本 `/home/simp/conf/client/createClic.sh` 生成机器码
3. 读取 `/home/audit/machine.info` 获取机器码
4. 构造许可证文件路径: `/home/licenses/{machineCode}_audit.lic`
5. 执行 `/home/simp/conf/client/checklic {licenseFile}` 验证许可证
6. 解析输出获取 LicenseInfo：
   - `licTime` — 许可证到期时间（毫秒）
   - `resultFlag` — 验证标志
   - `databaseInfo` — 授权数据库数
   - `dataLogInfo` — 数据日志量（单位：1亿）
   - `authorizedUser` — 授权用户
   - `softwareInfo` — 软件信息

### 运行时检查

- 每条消息处理前：`CodeCache.licenseInfo.expiryTime > proStartTime`
- 每条 ES 写入前：对比文档数与 `100,000,000 * dataLogInfo`

## 十、定时清理

### SaticScheduleTask

- Cron: `0 0 2 * * ?`（每日凌晨 2:00）
- 清理 `cleanTopics`: `aas-audit-log,aas-db-info,aas-flow-info,aas-sens-type,aas-classified-list`
- 使用 Kafka AdminClient，按分区获取最新 offset，删除 offset 之前的记录
- 结果记录到 `sys_log` 表

## 十一、线程池实现

所有线程池统一使用相同模式：

```java
new ThreadPoolExecutor(core, max, keepAlive, TimeUnit.MILLISECONDS,
    new ArrayBlockingQueue<>(queueSize), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy())
```

- 拒绝策略: `CallerRunsPolicy`（调用线程执行）
- 线程名: 带前缀 `thread-name + "-" + count`
- 关闭标志: `whetherToCloseA`, `whetherToCloseO`, `whetherToCloseC`, `apiWhetherToClose`（默认关闭）

## 十二、异常处理

### ExceptionHandleUtils

- 记录异常日志到 `/data/logs/simp_exception_data/`（`jsonExceptionDataDir`）
- 记录到 `sys_log` 表
- 异常消息发送通知（通过 Socket）
