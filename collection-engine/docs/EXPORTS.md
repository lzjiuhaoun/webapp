# EXPORTS.md — collection-engine 出口契约

> 版本: 1.0.0
> 维护: collection-engine 模块负责人

## 一、Kafka Topics 生产

### Topic 映射（数据库驱动）

| dataType | 含义 | Kafka Topic | 生产方式 |
|----------|------|-------------|---------|
| 0 | 非风险审计日志 | 数据库映射 | 批量发送 |
| 1 | 风险审计日志 | 数据库映射 | 批量发送 |
| 2 | 资产配置日志 | 数据库映射 | 逐条发送 |
| 3 | 资产流量日志 | 数据库映射 | 批量发送 |
| 9 | 设备告警 | aas-dev-alarm | 直接发送 |

**Topic 映射表**：`dev_data_type` 表通过 `topic_relation_info_id` 关联 `topic_relation_info` 表，实现动态 Topic 映射。

### 数据格式

| 数据类型 | 模型 | 格式 |
|---------|------|------|
| 0, 1, 3 | `CollectionModel` | JSON 字符串 `{dataType: "0", data: "...", operAction: "..."}` |
| 2 | `CollectionModel` | 逐条发送 |
| 9 | 自定义 | JSON 字符串 `{dataType: "9", ...}` |

## 二、设备配置模型

### SafetyDevConfig（安全设备配置）

| 字段 | 类型 | 说明 |
|------|------|------|
| devId | Integer | 设备 ID |
| devUuid | String | 设备 UUID |
| devName | String | 设备名称 |
| devTypeId | Integer | 设备类型（1=数据库审计, 2=漏洞扫描, 3=状态监控, 4=脱敏, 5=防火墙） |
| devEnable | Integer | 启用标志（1=启用） |
| devIp | String | 设备 IP |
| devPort | Integer | 设备端口 |
| collectionDataType | String | 采集数据类型（逗号分隔，如 "0,1,2,3"） |
| collectionMethod | Integer | 采集方法（1=Syslog） |
| collectionEncoding | String | 采集编码 |
| parserRules | String | 解析规则 |

### TopicRelationInfo（Topic 关系信息）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 主键 |
| kafkaTopic | String | Kafka Topic 名称 |
| logType | String | 日志类型 |
| logName | String | 日志名称 |
| dbType | String | 数据库类型 |
| dbName | String | 数据库名 |
| tableName | String | 表名 |
| tableFields | String | 表字段 |
| devType | String | 设备类型 |
| isRetrieval | String | 是否可检索 |
| extendA/B/C | String | 扩展字段 |

## 三、数据格式模型

### CollectionModel（采集模型）

| 字段 | 类型 | 说明 |
|------|------|------|
| dataType | String | 数据类型（0, 1, 2, 3, 9） |
| data | String | 消息内容（JSON 字符串） |
| operAction | String | 操作动作 |

### DataFormat（Sangfor 数据格式）

| 字段 | 类型 | 说明 |
|------|------|------|
| happenTime | String | 发生时间（Unix 秒） |
| engine_name | String | 引擎名称 |
| systemHost | String | 系统主机 |
| systemUser | String | 系统用户 |
| srcIp | String | 源 IP |
| srcPort | String | 源端口 |
| destIp | String | 目标 IP |
| destPort | String | 目标端口 |
| dbType | String | 数据库类型 |
| dbName | String | 数据库名 |
| dbUser | String | 数据库用户 |
| operType | String | 操作类型 |
| tableName | String | 表名 |
| operSentence | String | 操作语句 |
| rowNum | String | 影响行数 |
| dealState | String | 处理状态 |
| riskLev | String | 风险等级 |
| id | String | 记录 ID |

## 四、服务注册

- Zookeeper 路径: `/services/syslogserver`
- 注册数据: `{host}:{port}`
- 节点类型: `EPHEMERAL`（进程退出自动删除）

## 五、MyBatis Mapper 接口

| Mapper | 核心方法 | 用途 |
|--------|---------|------|
| `SafetyDevConfigMapper` | `selectSyslogDevBySrcIpAndPort(srcIp, srcPort)` | 按 IP 和端口查询设备 |
| `SafetyDevConfigMapper` | `selectSyslogDevBySrcIpAndDevType(srcIp, devTypeId)` | 按 IP 和设备类型查询设备 |
| `SafetyDevConfigMapper` | `selectDbType(dbValue)` | 查询数据库类型 ID |
| `TopicRelationInfoMapper` | `selectByLogType(logType)` | 按日志类型查询 Topic 映射 |

## 六、线程池配置

| 参数 | 值 | 说明 |
|------|-----|------|
| Core Pool Size | 5 | 核心线程数 |
| Max Pool Size | 5 | 最大线程数 |
| Queue Capacity | 150 | 任务队列容量 |
| Rejection Policy | AbortPolicy | 拒绝策略（抛出异常） |
