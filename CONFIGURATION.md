# CONFIGURATION.md — 配置详解

## 概述

系统各模块都有丰富的配置项，以下列出所有模块的关键配置。

---

## web-app 配置

### 应用配置 (application.yml)

**Profile**: `dev` (默认), `prod` (与 dev 配置相同)

**服务器配置**:
- 名称: `webapp`
- 主机: `simp`
- 端口: `8443`
- SSL: 启用 (JKS 密钥库，密码 `123456`，仅 TLSv1.2)
- Tomcat: 连接超时 3000ms，最大线程 500

**Socket 服务器** (接收数据):
- 主机: `localhost`
- 端口: `9095`

**备份系统**:
- 全量备份: `/data/backup/full`，cron `0 0 2 * * ?` (每日凌晨 2 点)
- 增量备份: `/data/backup/incremental`，cron `0 0 */6 * * ?` (每 6 小时)
- 加密: SM4，密钥 `WrSFUWPQ12XMcv6h7lSMow==`，默认关闭
- 保留: 30 天
- MySQL bin 路径: `C:\Program Files\mysql-5.7.21\bin\`

**Elasticsearch**:
- 集群: `simp_cluster`
- REST URI: `simp:9300`，用户名: `elastic`，密码: 加密 `rSUm+tQczfpRrYTSfJQpbw==`
- 额外配置: `172.19.6.75:9200`，用户名: `elastic`，密码: `Chinglin@9527`

**OEM 数据库** (MySQL):
- 主机: `172.19.6.75:3306`，用户名: `root`，密码: `Chinglin@9527`
- 数据库: `gateway_admin`，版本: 8

**主数据库** (MySQL):
- URL: `jdbc:mysql://localhost:23306/aas_simp?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&tinyInt1isBit=false&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai`
- 用户名: `sroot`，密码: `Ankki_mySQL123`
- 驱动: `com.mysql.jdbc.Driver`
- 类型: Druid 连接池
- Druid 连接池: initial=5, minIdle=5, maxActive=2000, maxWait=2000ms

**Kafka**:
- Bootstrap servers: `simp:9092`
- 安全: SASL_PLAINTEXT，机制 PLAIN
- JAAS 配置: username `admin`，password `Ankki_Kafka123`
- Producer: acks=all, retries=3, batch-size=16384
- Consumer: group-id `inst-ack-group`，auto-offset-reset=earliest

**Quartz 调度**:
- 存储类型: JDBC
- 线程数: 10，优先级: 5
- Job store: LocalDataSourceJobStore，clustered=true

**线程池配置**:
- DB 发现: corePoolSize=5
- 敏感数据发现: corePoolSize=5
- 数据清理: corePoolSize=1, maxPoolSize=10
- BI 任务: corePoolSize=5

**限制**:
- Excel 导出: 每 Sheet 60000 行
- 日志存储: 10,000,000
- 搜索最大: 100,000
- 最大 DB 配置: 128
- 最大数据字典: 128
- 最大任务: 128
- 告警策略最大: 128
- 安全模型最大: 128
- 安全规则最大: 2000
- 物理位置最大: 1024
- 逻辑区域最大: 1024
- 组织级别 1 最大: 32
- 组织级别 2 最大: 64
- 经理最大: 2000

**CAS SSO**:
- 服务器 URL: `https://authserver.imvcc.edu.cn/authserver/login`
- 认证 URL: `https://authserver.imvcc.edu.cn/authserver`
- 客户端主机: `https://172.19.6.160:8443/`
- 默认用户: `secadmin`

**钉钉**:
- 租户 ID: `50543168`
- App Key: `DSAMS-ZdkSAMz4MEUpOZoi9VBbANJb`
- App Secret: 加密

**API 审计系统**:
- 数据库端口: 3306，账号: root，密码: `Chinglin@9527`
- 数据库示例: `gateway_admin`，版本: 8
- URL 同步: false，用户行为: true

**MyBatis 加密器**:
- 启用: true，算法: BASE64，编码: BASE64

**API 解密**:
- 启用: true，Header 标志: `encrypt-key`
- SM2/RSA 加密的公钥/私钥

**文件上传**:
- 上传目录: `/data/file-upload/`
- 允许类型: `.txt`, `.log`, `.json`, `.csv`
- 最大文件大小: 100MB

**数据流**:
- 自动生成数据流图: 启用

---

## kafka-client 配置

### Kafka 消费者
- Bootstrap: `simp:9092`
- Groups: `auditContainer`, `dbStaticContainer`, `classifiedContainer`, `dmContainer`, `apiAuditContainer`, `aumdq`
- Auto commit: false，interval: 1000ms
- Session timeout: 15000ms，max poll records: 500
- Auto offset reset: earliest
- SASL: 启用，username `admin`，password `Ankki_Kafka123`

### Topic 配置
- Other: `aas-db-info,aas-flow-info,aas-sens-type,aas-dev-alarm`
- Audit log: `aas-audit-log`
- API audit log: `aas-api-audit-log`
- DB asset: `aas-db-info`，flow: `aas-flow-info`
- Sens type: `aas-sens-type`，classified list: `aas-classified-list`
- DM topics: `sdm-database-config,sdm-datamask-db-task,sdm-datamask-file-task`
- Dev alarm: `aas-dev-alarm`

### Elasticsearch
- Cluster: `simp_cluster`，host: `simp`，transport port: 9300
- Index: `aassimp-auditlog`，API index: `aassimp-apiauditlog`
- Type: `aasauditlog`，security user: `elastic:Ankki_ES123`
- Port: 9200，password: 加密 `rSUm+tQczfpRrYTSfJQpbw==`

### 线程池
- Audit log: core=2, max=3, keepAlive=30s, queue=8
- API audit: core=2, max=3, keepAlive=30s, queue=8
- Other log: core=3, max=4, keepAlive=30s, queue=8
- DM: core=2, max=3, keepAlive=30s, queue=8
- Classified: core=2, max=3, keepAlive=30s, queue=8

### 数据库 (MySQL)
- URL: `jdbc:mysql://localhost:23306/aas_simp?...`
- Username: `sroot`，password: `Ankki_mySQL123`

### 许可证
- Check cron: `0 0 0/1 * * ?` (每小时)
- Config path: `/home/simp/conf/`
- Machine code: `/home/audit/`
- License path: `/home/licenses/`

---

## collection-engine 配置

### 数据库 (MySQL)
- URL: `jdbc:mysql://127.0.0.1:23306/aas_simp?...`
- Username: `sroot`，password: `Ankki_mySQL123`
- Druid: initial=5, minIdle=5, maxActive=20

### Syslog 服务器
- Host: `simp`，port: 9514
- Cache sizes: risk=1, noRisk=1, dbFlow=1, dbConf=1, devAlarm=1
- Kafka: `simp:9092`，SASL 启用，username `admin`，password `Ankki_Kafka123`
- Thread pool: corePoolSize=5, maxPoolSize=5, maxThreadNum=150
- Netty: 启用 (端口 8443)，UDP buffer 10MB
- Data handler: 启用

---

## manager-center 配置

### 数据库 (MySQL)
- URL: `jdbc:mysql://127.0.0.1:23306/aas_simp?...`
- Username: `sroot`，password: `Ankki_mySQL123`
- Druid: initial=5, minIdle=5, maxActive=20

### Socket 服务器
- Name: `managercenter`，host: `localhost`，port: `9090`
- Client: TCP noDelay=true，timeout 2000000ms

### 线程池
- Core size: 6, max thread size: 6, keepAlive: 1800s, queue: 50

---

## report-engine 配置

### Elasticsearch
- Cluster: `simp_cluster`，nodes: `simp:9300`
- Security: `elastic:Ankki_ES123`

### 数据库 (MySQL)
- URL: `jdbc:mysql://127.0.0.1:23306/aas_simp?...`
- Username: `sroot`，password: `Ankki_mySQL123`

### 统计
- Cron: `0 30 1 * * ?` (每日 1:30AM)
- Report: `0 0/10 * * * ?` (每 10 分钟)
- Suspicious list: `0 0 0/1 * * ?` (每小时)
- Daily report: `0 30 1 */5 * ?` (每 5 天)
- ES terms size: 128

---

## daemon 配置

### 服务
- Name: `daemon`，host: `127.0.0.1:10000`
- Watch services: `reportengine,syslogserver,webapp,managercenter,matchAnalysis,behaviorAnalysis,kafkaclient,kafkaclient1,...,kafkaclient5`

---

## socket-comm 配置

### Socket 客户端
- Management center: host `localhost`，port `9090`
- Client: TCP noDelay=true，timeout 2000000ms，retries=2

### 线程池
- Core size: 6, max thread size: 6, keepAlive: 1800s, queue: 50
