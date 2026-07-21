# EXPORTS.md — kafka-client 出口契约

> 版本: 1.0.0
> 维护: kafka-client 模块负责人

## 一、Kafka Topics 消费

| # | Topic | 消费者组 | 并发数 | 处理类 | 输出存储 |
|---|-------|---------|--------|--------|---------|
| 1 | `aas-audit-log` | `auditContainer` | 5 | ProcessAuditRecord | ES: aassimp-auditlog |
| 2 | `aas-api-audit-log` | `apiAuditContainer` | 5 | ProcessApiAuditRecord | ES: aassimp-apiauditlog |
| 3 | `aas-db-info` | `dbStaticContainer` | 5 | ProcessOtherRecord | MySQL: db_conf, db_judge |
| 4 | `aas-flow-info` | `dbStaticContainer` | 5 | ProcessOtherRecord | MySQL: db_asset_flow_session |
| 5 | `aas-sens-type` | `dbStaticContainer` | 5 | ProcessOtherRecord | MySQL: sens_type, sens_type_detail |
| 6 | `aas-dev-alarm` | `dbStaticContainer` | 5 | ProcessOtherRecord | MySQL: dev_alarm_history |
| 7 | `aas-classified-list` | `classifiedContainer` | 5 | ProcessClassifiedRecord | MySQL: 8+ 表 |
| 8 | `sdm-database-config` | `dmContainer` | 5 | ProcessDmRecord | MySQL: db_conf, db_judge |
| 9 | `sdm-datamask-db-task` | `dmContainer` | 5 | ProcessDmRecord | MySQL: dm_db_task + 2 结果表 |
| 10 | `sdm-datamask-file-task` | `dmContainer` | 5 | ProcessDmRecord | MySQL: dm_file_task |
| 11 | `aas-p-log` | `aumdq` | 5 | AntiUnifiedQueryRecord | ES: aassimp-auditlog |

## 二、数据解析策略

### Audit Log 策略（DataAnalysisStrategy）

| 策略实现 | 数据格式 | 说明 |
|---------|---------|------|
| `JsonDataAnalysis`（默认） | `{...}` 或 `[...]` JSON | 单条/数组审计日志，自动检测格式 |
| `ListDataAnalysis` | `[{...}]` JSON 列表 | 列表格式审计日志 |

配置: `dataAnalysisStrategy=JsonDataAnalysis`

### API Audit 策略（ApiDataAnalysisStrategy）

| 策略实现 | 数据格式 | 说明 |
|---------|---------|------|
| `ApiV1JsonAnalysis`（默认） | `{...}` JSON | Ankki OEM API v1.0 |
| `ApiV2JsonAnalysis` | N/A | OEM API v2.0 占位符 |

配置: `apiDataAnalysisStrategy=ApiV1JsonAnalysis`

### Anti-Unified Query 策略（DataPAnalysisStrategy）

| 策略实现 | 数据格式 | 说明 |
|---------|---------|------|
| `JsonDataPAnalysis`（默认） | `{...}` 或 `[...]` JSON | 防统一查询审计日志 |

配置: `dataPAnalysisStrategy=JsonDataPAnalysis`

## 三、Elasticsearch 写入

### 索引: `aassimp-auditlog`

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | 消息唯一标识 |
| happenTime | String | 发生时间 |
| srcIp / srcPort / srcMac | String | 源 IP/端口/MAC |
| systemUser / systemHost | String | 系统用户/主机 |
| destIp / destPort / destMac | String | 目标 IP/端口/MAC |
| visitTool | String | 访问工具 |
| appAccount | String | 应用账号 |
| sessionId | String | 会话 ID |
| dbType / dbUser / dbName | String | 数据库类型/用户/名称 |
| operType / tableName / fieldName | String | 操作类型/表名/字段 |
| operSentence | String | 操作语句 |
| sqlBindValue | String | SQL 绑定值 |
| rowNum / sqlExecTime | Long/Double | 行数/执行时间 |
| sqlResponse / returnType | String | SQL 响应/返回类型 |
| returnContent | String | 返回内容 |
| dealState | String | 处理状态 |
| ruleType / ruleName | String | 规则类型/名称 |
| riskLev | String | 风险等级 |
| hwId / devUuid | String | 硬件ID/设备UUID |
| sensData | Integer | 敏感数据标记(1=是) |
| dbTypeStr / deviceIp / schemaName | String | 数据库类型名/设备IP/模式 |
| tenantName | String | 租户名称 |

许可证限制: `100,000,000 * dataLogInfo` 文档数

### 索引: `aassimp-apiauditlog`

| 字段 | 类型 | 说明 |
|------|------|------|
| id ~ destPort | String | 同 aassimp-auditlog |
| srcIpOrigin | String | 源 IP 原始地址 |
| appName | String | 应用名称 |
| operType ~ returnContent | String | 同 aassimp-auditlog |
| returnType | String | 返回类型 |
| protocol | String | 协议 |
| apiName / apiUrl / apiMethod | String | API 名称/URL/方法 |
| apiUserAgent / apiDomainName | String | API 用户代理/域名 |
| apiMd5 | String | API MD5 |

## 四、MySQL 写入

| 表名 | Mapper | 来源 |
|------|--------|------|
| `db_conf` | DbConfMapper | aas-db-info, sdm-database-config |
| `db_judge` | DbJudgeMapper | aas-db-info, sdm-database-config |
| `db_asset_flow_session` | DbAssetFlowSessionMapper | aas-flow-info |
| `sens_type` | SensTypeMapper | aas-sens-type |
| `sens_type_detail` | SensTypeDetailMapper | aas-sens-type |
| `dev_alarm_history` | DevAlarmHistoryMapper | aas-dev-alarm |
| `classified` | ClassifiedMapper | aas-classified-list |
| `database_info` | DatabaseInfoMapper | aas-classified-list |
| `db_statistics_info` | DbStatisticsInfoMapper | aas-classified-list |
| `find_task` | FindTaskMapper | aas-classified-list |
| `find_info` | FindInfoMapper | aas-classified-list |
| `find_details` | FindDetailsMapper | aas-classified-list |
| `db_conf_extend` | DbConfExtendMapper | aas-classified-list (Oracle) |
| `dm_db_task` | DmDbTaskMapper | sdm-datamask-db-task |
| `dm_desen_table_result` | DmDesenTableResultMapper | sdm-datamask-db-task |
| `dm_desen_field_result` | DmDesenFieldResultMapper | sdm-datamask-db-task |
| `dm_file_task` | DmFileTaskMapper | sdm-datamask-file-task |
| `safety_dev_config` | SafetyDevConfigMapper | 多来源引用 |
| `change_log` | ChangeLogMapper | 资产同步操作 |

## 五、线程池配置

| 线程池 | Core | Max | KeepAlive | Queue | 用途 |
|--------|------|-----|-----------|-------|------|
| AuditRecordThreadPool | 2 | 3 | 30000ms | 8 | 审计日志 |
| ApiAuditRecordThreadPool | 2 | 3 | 30000ms | 8 | API 审计 |
| OtherRecordThreadPool | 3 | 4 | 30000ms | 8 | 资产/流量/敏感/告警 |
| ClassifiedRecordThreadPool | 2 | 3 | 30000ms | 8 | 分类分级 |
| DmRecordThreadPool | 2 | 3 | 30000ms | 8 | 脱敏任务 |
| AntiUnifiedQueryThreadPool | 2 | 3 | 30000ms | 8 | 防统一查询 |

## 六、服务注册

- Zookeeper 路径: `/services/kafkaclient`
- 注册数据: `{host}:{port}`
- 许可证监控: `/data/licenseUpdateTime`
