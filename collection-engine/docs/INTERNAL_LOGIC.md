# INTERNAL_LOGIC.md — collection-engine 内部实现逻辑

> 本模块描述 collection-engine 的内部实现细节。

## 一、启动流程

### 1. Spring Boot 启动
- `SimpsyslogserverApplication.main()` 启动
- `CommandLineRunner.run()` 注册 `syslogserver` 到 Zookeeper（host: `127.0.0.1:10000`）
- `SystemStart.run()` 启动 UDP 服务器

### 2. UDP 服务器启动（Netty 模式，默认）
- 从 `syslogconf.properties` 读取 `nettySysLogSever=1`
- 创建 `NioEventLoopGroup()` 事件循环线程池
- 创建 `Bootstrap` 绑定 `NioDatagramChannel`
- 设置 handler 为 `NettyUdpChannelInHandler`
- 配置 socket 选项：
  - `SO_BROADCAST = true`（支持广播）
  - `SO_RCVBUF = 10MB`（接收缓冲区）
  - `SO_SNDBUF = 10MB`（发送缓冲区）
- 绑定 `simp:9514`（serverHost:serverPort）
- 阻塞等待连接

### 3. 数据接收处理
- `NettyUdpChannelInHandler.channelRead0()` 接收 DatagramPacket
- 提取 IP、端口、数据（UTF-8 字符串）
- 检查 `nettyDataHandler=1`（启用数据处理）
- 调用 `nettySyslogService.dataHandler()`（异步）

## 二、Syslog 数据解析与分类

### 数据分类逻辑（NettySyslogService.dataHandler）

**步骤 1：判断数据类型**

1. **标准 Syslog 数据**（包含 "happenTime"）
   - 解析 JSON 为 `CollectionModel`
   - 提取 `dataType` 字段（"0", "1", "2", "3"）
   - 查询设备配置（`SafetyDevConfigMapper.selectSyslogDevBySrcIpAndPort`）
   - 检查设备的 `collectionDataType` 是否包含该 dataType
   - 根据 dataType 分类到不同列表：
     - "0" → `noRiskList`（非风险审计日志）
     - "1" → `riskList`（风险审计日志）
     - "2" → `dbConfList`（资产配置日志）
     - "3" → `dbFlowList`（资产流量日志）

2. **Sangfor 设备数据**（包含 "record_id" AND "record_time"）
   - 调用 `getSXFString()` 转换为标准格式
   - 设置 `dataType = "1"`（风险日志）
   - 查询设备配置
   - 若设备允许采集风险日志，添加到 `riskList`

3. **告警数据**（包含 "alarmType" OR "DBAudit"）
   - 对于 "DBAudit"（防火墙告警）：按 IP 和 dev_type_id=5 查询设备
   - 对于 "alarmType"（脱敏告警）：按 IP 和 dev_type_id=5 查询设备
   - 设置 `dataType = "9"`，直接发送

**步骤 2：批量检查与 Kafka 生产**

- 对每种数据类型，检查列表大小是否达到批量阈值（`riskMaxNum`, `noRiskMaxNum` 等，默认均为 1）
- 若达到阈值：创建 `NettyKafkaProduce(logType, list, config, mapper)`，调用 `run()`
- 清空列表

### 数据分类常量（Constants.java）

| 常量 | 值 | 含义 |
|------|-----|------|
| `THE_DEV_DATA_TYPE_NORISKAUDITLOG` | 0 | 非风险审计日志 |
| `THE_DEV_DATA_TYPE_RISKAUDITLOG` | 1 | 风险审计日志 |
| `THE_DEV_DATA_TYPE_DBCONFLOG` | 2 | 资产配置日志 |
| `THE_DEV_DATA_TYPE_DBFLOWLOG` | 3 | 资产流量日志 |
| `THE_DEV_DATA_TYPE_DEVALARM` | 90 | 能力单元告警 |

## 三、Kafka 生产实现

### NettyKafkaProduce（Netty 模式）

**构造函数**：
- 创建 KafkaProducer（SASL_PLAINTEXT 认证）
- 配置：`acks=all`, `retries=0`, `batch.size=16384`
- 超时配置：`request.timeout.ms=10000`, `max.block.ms=10000`
- 序列化器：`StringSerializer`

**run() 方法**：
- 对 dataType 0, 1, 3：发送整个列表为单条消息（`list.toString()`），随机 key（0 或 1）
- 对 dataType 2：遍历列表，逐条发送
- 对 dataType 90：使用 `JSONUtils.toJSONString(data)` 序列化列表
- 发送后 `Thread.sleep(5)`
- finally 块中关闭 producer

### KafkaProduce（Legacy 模式）

**线程池**：`ThreadPoolExecutor(core=5, max=5, queue=150, AbortPolicy)`

**run() 方法**：
- 对 dataType 0, 1, 3：发送整个列表为单条消息（`list.toString()`），随机 key（0 或 1）
- 对 dataType 2：遍历列表，逐条发送
- 发送后 `Thread.sleep(100)`
- finally 块中关闭 producer

## 四、Sangfor 设备数据格式转换

### getSXFString() 方法

**输入格式**（原始 Sangfor 数据）：
```
date: 2024-01-01 12:00:00 record_time: 1234567890 data_base_manage: xxx dev: xxx
src_ip: 1.2.3.4 src_port: 12345 dst_ip: 5.6.7.8 dst_port: 3306
db_type: mysql db_name: testdb db_usr: admin
sql: SELECT * FROM table WHERE id=1 operate: query aff_rows: 10 ret_id: 0 record_id: 123
```

**转换过程**：
1. 提取 `happenTime`（date 和 record_time 字段，转换为 Unix 秒）
2. 设置 `engine_name = "xsq"`（占位符）
3. 提取 `systemHost`（data_base_manage）
4. 提取 `systemUser`（dev）
5. 提取 `srcIp`, `srcPort`, `destIp`, `destPort`
6. 查询 `db_type` 对应的数据库类型 ID
7. 提取 `dbName`, `dbUser`, `tableName`
8. 提取 `operSentence`（sql）
9. 提取 `operType`（operate 或 sql 第一个词）
10. 提取 `rowNum`（aff_rows）
11. 提取 `dealState`（ret_id）
12. 提取 `id`（record_id）
13. 设置随机 `riskLev`（0-3）
14. 设置其他默认值（srcMac, destMac, fieldName 等）

**输出**：拼接为逗号分隔的 JSON 字符串（无外层花括号），外层添加 `dataType=1`

## 五、设备配置管理

### SafetyDevConfigMapper 查询

**selectSyslogDevBySrcIpAndPort(srcIp, srcPort)**
```sql
SELECT * FROM safety_dev_config
WHERE dev_enable=1 AND dev_ip=? AND dev_port=?
```
用于标准 Syslog 数据查询设备。

**selectSyslogDevBySrcIpAndDevType(srcIp, devTypeId)**
```sql
SELECT * FROM safety_dev_config
WHERE dev_enable=1 AND dev_ip=? AND dev_type_id=? LIMIT 1
```
用于防火墙/脱敏告警数据查询设备。

**selectDbType(dbValue)**
```sql
SELECT db_type_id FROM database_types WHERE db_type_value=?
```
查询数据库类型 ID。

### TopicRelationInfoMapper 查询

**selectByLogType(logType)**
```sql
SELECT b.* FROM dev_data_type AS a, topic_relation_info AS b
WHERE a.data_type_id = ? AND a.topic_relation_info_id = b.id
```
按日志类型查询 Kafka Topic 映射。

## 六、数据流总览

```
UDP 设备 → NettyUdpServer(9514) → NettyUdpChannelInHandler
  → NettySyslogService.dataHandler() [异步]
    → 解析 JSON / Sangfor 格式 / 告警格式
    → 查询设备配置
    → 分类到 noRiskList / riskList / dbConfList / dbFlowList
    → 批量检查
    → NettyKafkaProduce.run()
      → KafkaProducer
        → 发送消息到 Kafka Topic
```

## 七、Legacy 模式（sysLogDataHandlerOld=1）

- Netty 接收数据后调用 `syslogService.dataHandler()`（同步）
- SyslogService 内部使用线程池执行 KafkaProduce
- KafkaProduce.run() 中创建 KafkaProducer，发送消息
- 发送后 `Thread.sleep(100)`
- 线程池拒绝策略：AbortPolicy（抛出异常）

## 八、异常处理

- Netty 接收异常：记录日志，继续接收
- 解析异常：记录日志，丢弃消息
- 设备配置查询失败：记录日志，不发送消息
- Kafka 发送异常：记录日志，关闭 producer
- 线程池拒绝：抛出 RejectedExecutionException（AbortPolicy）

## 九、配置管理

### syslogconf.properties 关键配置

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| serverHost | simp | 服务器地址 |
| serverPort | 9514 | UDP 监听端口 |
| riskMaxNum | 1 | 风险日志批量大小 |
| noRiskMaxNum | 1 | 非风险日志批量大小 |
| dbFlowMaxNum | 1 | 资产流量日志批量大小 |
| dbConfMaxNum | 1 | 资产配置日志批量大小 |
| devAlarmMaxNum | 1 | 设备告警批量大小 |
| kafkaIpPort | simp:9092 | Kafka 地址 |
| openSasl | yes | 启用 SASL 认证 |
| username | admin | Kafka 用户名 |
| password | Ankki_Kafka123 | Kafka 密码 |
| syslogCorePoolSize | 5 | 线程池核心大小 |
| syslogCorePoolMaxSize | 5 | 线程池最大大小 |
| maxThreadNum | 150 | 线程池队列容量 |
| nettySysLogSever | 1 | 启用 Netty 服务器 |
| nettySoRcvbuf | 10 | 接收缓冲区（MB） |
| nettySoSndBuf | 10 | 发送缓冲区（MB） |
| nettyDataHandler | 1 | 启用数据处理 |
| sysLogDataHandlerOld | 0 | 使用 NettySyslogService |
| aasDevAlarmTopic | aas-dev-alarm | 设备告警 Topic |
