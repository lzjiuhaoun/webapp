# KAFKA_TOPIC_SCHEMA.md — Kafka Topic 消息格式

## 概述

系统使用 Kafka 作为消息总线，Topic 由数据库 `topic_relation_info` 表动态映射。以下列出所有 Topic 的消息格式。

---

## 1. aas-audit-log — 审计日志

**Producer**: collection-engine  
**Consumer**: kafka-client (auditContainer)  
**消息格式**: JSON 字符串  
**Key**: safetyDevConfig.getDevUuid() (能力单元 UUID)  
**Partition**: new Random().nextInt(2) (0-2 分区)

### AuditRecord 模型

```json
{
  "id": "String",
  "AUDIT_TIME": "Long (秒级时间戳)",
  "srcIp": "String",
  "srcPort": "Integer",
  "srcMac": "String",
  "systemUser": "String",
  "systemHost": "String",
  "destIp": "String",
  "destPort": "Integer",
  "destMac": "String",
  "SRC_VISIT_TOOL": "String",
  "appAccount": "String",
  "sessionId": "String",
  "dbType": "Integer",
  "LOG_USR": "String",
  "operType": "String",
  "dbName": "String",
  "TABLE_NAME": "String",
  "COLUM_NAME": "String",
  "operSentence": "String",
  "operSentenceLen": "Integer",
  "sqlBindValue": "String",
  "rowNum": "Integer",
  "sqlExecTime": "String",
  "sqlResponse": "Integer",
  "returnContent": "String",
  "RETURN_COUNT": "Integer",
  "dealState": "Integer",
  "protectObjectName": "String",
  "ruleType": "Integer",
  "ruleName": "String",
  "riskLev": "Integer",
  "hwId": "String",
  "devUuid": "String",
  "indexTime": "Long",
  "sensData": "Integer",
  "dbTypeStr": "String",
  "RETURN_CONTENT": "List<List<String>>",
  "relatedInfo": "String",
  "tenantName": "String",
  "schemaName": "String",
  "deviceIp": "String"
}
```

---

## 2. aas-api-audit-log — API 审计日志

**Producer**: kafka-client (apiAuditContainer)  
**消息格式**: JSON 字符串  
**Key**: safetyDevConfig.getDevUuid()  
**Partition**: new Random().nextInt(2)

### ApiV1AuditRecord 模型

```json
{
  "id": "Long",
  "happenTime": "Long (秒级时间戳)",
  "srcIp": "String",
  "srcPort": "Integer",
  "srcMac": "String",
  "srcIpOrigin": "String",
  "systemUser": "String",
  "systemHost": "String",
  "destIp": "String",
  "destPort": "Integer",
  "destMac": "String",
  "visitTool": "String",
  "appAccount": "String",
  "appName": "String",
  "sessionId": "String",
  "operType": "String",
  "dbName": "String",
  "tableName": "String",
  "fieldName": "String",
  "operSentence": "String",
  "operSentenceLen": "Integer",
  "sqlBindValue": "String",
  "rowNum": "Integer",
  "sqlExecTime": "Double",
  "sqlResponse": "Integer",
  "returnType": "String",
  "returnContent": "String",
  "returnContentLen": "Integer",
  "dealState": "Integer",
  "protectObjectName": "String",
  "ruleType": "String",
  "ruleName": "String",
  "riskLev": "Integer (0-高风险,1-中风险,2-低风险,3-关注,4-一般)",
  "devUuid": "String",
  "protocol": "String",
  "apiName": "String",
  "apiUrl": "String",
  "apiMethod": "String",
  "apiUserAgent": "String",
  "apiDomainName": "String",
  "apiMd5": "String",
  "extend1": "String",
  "extend2": "String",
  "extend3": "String",
  "tenantName": "String"
}
```

---

## 3. aas-classified-list — 分类分级清单

**Producer**: kafka-client (classifiedContainer)  
**消息格式**: JSON 字符串  
**Key**: safetyDevConfig.getDevUuid()  
**Partition**: new Random().nextInt(2)

### AssetClassifyLevel 模型

```json
{
  "id": "Integer",
  "tacticsId": "Integer",
  "dbUuid": "String",
  "dbName": "String",
  "host": "String",
  "port": "Integer",
  "dbTypeName": "String",
  "databaseName": "String",
  "tableName": "String",
  "tableDescribe": "String",
  "tableCategoryName": "String",
  "tableLevelName": "String",
  "fieldName": "String",
  "fieldDescribe": "String",
  "fieldType": "String",
  "fieldLength": "String",
  "fieldClass": "String",
  "fieldCategoryName": "String",
  "fieldLevelName": "String",
  "fieldLevelValue": "Integer",
  "matchingRate": "String",
  "matchingResult": "String",
  "sensId": "String",
  "schema": "String",
  "businessSystem": "String",
  "organization": "String",
  "scanTime": "String",
  "md5": "String"
}
```

---

## 4. sdm-database-config — 脱敏数据库配置

**Producer**: kafka-client (dmContainer)  
**消息格式**: JSON 字符串  
**Key**: safetyDevConfig.getDevUuid()  
**Partition**: new Random().nextInt(2)

### DmTaskInfo 模型

```json
{
  "devUuid": "String",
  "operType": "Integer (0添加/1修改/2删除)",
  "dbTaskList": [
    {
      "dmDbId": "Integer",
      "dbTaskId": "Long",
      "dbTaskName": "String",
      "targetType": "Integer",
      "taskStartTime": "Long",
      "taskEndTime": "Long",
      "taskStatus": "String",
      "srcDbConfigIp": "String",
      "srcDbConfigPort": "Integer",
      "srcDbConfigSid": "String",
      "srcDbConfigType": "String",
      "srcDbConfigDbExample": "String",
      "tarDbConfigIp": "String",
      "tarDbConfigPort": "Integer",
      "tarDbConfigSid": "String",
      "tarDbConfigType": "String",
      "tarDbConfigDbExample": "String",
      "tarFileName": "String",
      "devUuid": "String",
      "isDelete": "Integer"
    }
  ],
  "fileTaskList": [
    {
      "dmFileId": "Integer",
      "fileTaskId": "Long",
      "fileTaskName": "String",
      "targetType": "Integer",
      "taskStartTime": "Long",
      "taskEndTime": "Long",
      "taskStatus": "String",
      "srcFileName": "String",
      "tarFileName": "String",
      "tarDbConfigIp": "String",
      "tarDbConfigPort": "Integer",
      "tarDbConfigSid": "String",
      "tarDbConfigType": "String",
      "tarDbConfigDbExample": "String",
      "devUuid": "String",
      "isDelete": "Integer"
    }
  ],
  "dbTaskIds": "Long[]",
  "fileTaskIds": "Long[]"
}
```

---

## 5. sdm-datamask-db-task — 脱敏数据库任务

**Producer**: kafka-client (dmContainer)  
**消息格式**: JSON 字符串  
**Key**: safetyDevConfig.getDevUuid()  
**Partition**: new Random().nextInt(2)

---

## 6. sdm-datamask-file-task — 脱敏文件任务

**Producer**: kafka-client (dmContainer)  
**消息格式**: JSON 字符串  
**Key**: safetyDevConfig.getDevUuid()  
**Partition**: new Random().nextInt(2)

---

## 7. aas-p-log — 防统方日志

**Producer**: kafka-client (aumdq)  
**消息格式**: 与审计日志类似，由 AntiUnifiedQueryRecord 处理

---

## 8. aas-dev-alarm — 能力单元告警

**Producer**: collection-engine  
**消息格式**: JSON 字符串  
**Key**: safetyDevConfig.getDevUuid()  
**Partition**: new Random().nextInt(2)

### DevAlarmHistory 模型

```json
{
  "id": "Long",
  "devUuid": "String",
  "devTypeId": "Integer",
  "devIp": "String",
  "devPort": "Integer",
  "alarmType": "String",
  "message": "String",
  "generatedTime": "Long (yyyy-MM-dd HH:mm:ss)",
  "reportTime": "Long (yyyy-MM-dd HH:mm:ss)",
  "createTime": "Long"
}
```

---

## Producer 配置

**Producer 类**: KafkaProduce / NettyKafkaProduce

**配置项**:
```properties
bootstrap.servers = simp:9092
acks = all
retries = 0
batch.size = 16384
key.serializer = StringSerializer
value.serializer = StringSerializer
MAX_REQUEST_SIZE_CONFIG = 5242880 (5MB)
```

**SASL 配置**:
- 通过 `openSasl=yes` 开启
- 使用 SASL_PLAINTEXT 安全认证
- JAAS 配置: username `admin`, password `Ankki_Kafka123`

**发送方式**:
- 使用 ProducerRecord 发送
- Key: safetyDevConfig.getDevUuid()
- Partition: new Random().nextInt(2)
- Value: 数据列表转字符串 (logType 0/1/3) 或逐条发送 (logType 2)

## Consumer 配置

**Consumer 类**: ConsumerListener

**监听配置**:
```properties
groupId = auditContainer
groupApi = apiAuditContainer
groupId2 = dbStaticContainer
groupClassified = classifiedContainer
groupDm = dmContainer
groupAumdq = aumdq
topics = aas-db-info,aas-flow-info,aas-sens-type,aas-dev-alarm
auditLogTopic = aas-audit-log
apiAuditLogTopic = aas-api-audit-log
aasClassifiedListTopic = aas-classified-list
dmTopics = sdm-database-config,sdm-datamask-db-task,sdm-datamask-file-task
aasPLogTopic = aas-p-log
consumeThreadNum = 5
```

**线程池配置**:
- auditExecutor: 审计记录线程池 (AuditRecordThreadPool)
- apiAuditExecutor: API 审计线程池 (ApiAuditRecordThreadPool)
- confingExecutor: 其他记录线程池 (OtherRecordThreadPool)
- classifiedExecutor: 分类分级线程池 (ClassifiedRecordThreadPool)
- confingExecutor: 脱敏记录线程池 (DmRecordThreadPool)
- confingExecutor: 防统方线程池 (AntiUnifiedQueryThreadPool)

**消费模式**:
- 批量消费 List<ConsumerRecord<?, ?>>
- 手动提交 Acknowledgment
- 并发数由 consumeThreadNum 配置
