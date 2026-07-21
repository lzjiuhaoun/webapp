# SHARED_DOMAIN.md — 通用语言

## 一、核心领域实体

### 1. 审计日志 (Audit Log)

**语义**：由数据库安全设备产生的操作记录，包含用户行为、SQL 语句、访问结果等信息。

**生命周期状态**：
- `RAW` — 原始 Syslog 报文，未经过解析
- `PARSED` — 经 collection-engine 分类后写入 Kafka
- `STORED` — 经 kafka-client 解析后存入 ES/MySQL
- `AGGREGATED` — 经 report-engine 聚合为统计报表

**关键属性**：
- `dataType` — 数据分类（0=非风险, 1=风险, 2=资产配置, 3=资产流量, 9=设备告警）
- `operType` — 操作类型
- `sourceIp` — 源 IP
- `destIp` — 目标 IP
- `userName` — 用户名
- `riskLevel` — 风险等级

### 2. 数据库资产 (Database Asset)

**语义**：平台所管理的数据库实例，包括类型、版本、IP、端口、所属区域等信息。

**生命周期状态**：
- `DISCOVERED` — 通过扫描发现
- `REGISTERED` — 录入平台管理
- `MONITORING` — 正在监控中
- `ARCHIVED` — 已归档

**支持的数据库类型**：MySQL, Oracle, PostgreSQL, DB2, SQL Server, Sybase, Informix, DM 达梦, Kingbase, Highgo, GBase, MongoDB, Hive

### 3. 安全设备 (Safety Device)

**语义**：对接的数据库安全设备（防火墙、脱敏设备等），通过 Syslog 上报数据。

**关键属性**：
- `deviceName` — 设备名称
- `deviceIp` — 设备 IP
- `dataType` — 上报数据类型
- `kafkaTopic` — 对应 Kafka Topic

### 4. 敏感数据类型 (Sensitive Data Type)

**语义**：数据库中敏感数据的分类标签（身份证号、手机号、银行卡号等）。

**关键属性**：
- `sensType` — 敏感类型
- `pattern` — 匹配规则
- `level` — 敏感等级

### 5. 告警 (Alarm)

**语义**：安全设备产生的告警事件或平台风险评估产生的告警。

**生命周期状态**：
- `PENDING` — 待处理
- `PROCESSING` — 处理中
- `RESOLVED` — 已解决
- `IGNORED` — 已忽略

### 6. 报表统计 (Report Statistics)

**语义**：由 report-engine 定时聚合产生的统计数据。

**关键属性**：
- `statTime` — 统计时间
- `statType` — 统计类型（用户操作、风险、流量等）
- `sourceIp` — 源 IP 维度
- `riskCount` — 风险计数
- `sensRiskCount` — 敏感风险计数

### 7. 消息路由 (Message Routing)

**语义**：manager-center 中维护的模块间消息路由规则。

**关键属性**：
- `messType` — 消息类型
- `contentType` — 内容类型
- `destHost` — 目标主机
- `destPort` — 目标端口

## 二、通用操作约定

| 操作 | 约定 |
|------|------|
| **增** | 创建记录时自动生成 `createTime` 时间戳 |
| **删** | 软删除为主，`isDelete` 标志字段 |
| **分页** | 统一使用 PageHelper，页码从 1 开始 |
| **密码** | 存储使用 SM4 加密 |
| **API 载荷** | 使用 SM2/RSA 非对称加密 |
| **时间格式** | `yyyy-MM-dd HH:mm:ss` |
| **字符集** | UTF-8 |

## 三、术语表

| 术语 | 含义 |
|------|------|
| AAS | Advanced Audit System |
| SIMP | Safety Integrated Management Platform |
| 态势感知 | 对数据库安全状态的整体感知和趋势预测 |
| 审计日志 | 安全设备记录的数据库操作日志 |
| 脱敏 | 对敏感数据进行遮盖或替换 |
| 数据库发现 | 自动扫描网络中的数据库实例 |
| 敏感数据发现 | 识别数据库中的敏感数据字段 |
| 风险评估 | 基于审计数据计算风险指标 |
| 预统计 | report-engine 对原始数据的定时聚合 |
