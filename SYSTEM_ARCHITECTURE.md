# SYSTEM_ARCHITECTURE.md — 系统架构

## 架构总览

系统采用**多进程联邦架构**，由 6 个独立部署的 JVM 进程 + 2 个共享库模块组成。进程间通过原始 TCP Socket 通信，Zookeeper 作为服务注册与心跳监控中心，Kafka 作为消息总线，Elasticsearch 和 MySQL 作为存储层。

```
┌──────────────────────────────────────────────────────────────┐
│                        External World                        │
│  ┌────────────┐  ┌──────────────┐  ┌────────────────────┐   │
│  │Security    │  │Database      │  │Web Browser,        │   │
│  │Devices     │  │Assets        │  │CAS SSO, APIs       │   │
│  │(Syslog)    │  │(Oracle,DB2,  │  │                    │   │
│  │            │  │ SQLServer...)│  │                    │   │
│  └─────┬──────┘  └──────┬───────┘  └────────┬───────────┘   │
└────────┼───────────────┼────────────────────┼───────────────┘
         │ UDP Syslog     │ JDBC Discovery      │ HTTPS 8443
         ▼                │                     │
┌──────────────────────────────────────────────────────────────┐
│                    AAS-SIMP Platform                         │
│                                                              │
│  ┌─────────────────┐    ┌─────────────────┐                 │
│  │collection-engine│    │   web-app        │                 │
│  │ UDP Syslog_recv │    │ REST + WebSocket │                 │
│  │ Kafka_produce   │    │ CAS + Swagger    │                 │
│  │ :UDP            │    │ :8443/:9095      │                 │
│  └───────┬─────────┘    └────────┬─────────┘                 │
│          │ Kafka                  │ Socket                    │
│          ▼                        ▼                           │
│  ┌─────────────────┐    ┌─────────────────┐                 │
│  │  kafka-client   │    │ manager-center   │                 │
│  │ Kafka_consume   │    │ Socket Router    │                 │
│  │ ES_write        │    │ :9090            │                 │
│  │ MySQL_write     │    └────────┬─────────┘                 │
│  └────────┬────────┘             │ Socket routing            │
│           │                      ├────┬────┬────┬────┐       │
│           ▼                      │    │    │    │    │       │
│  ┌─────────────────┐             ▼    ▼    ▼    ▼    ▼       │
│  │ report-engine   │      ┌──────┐┌────┐┌──────┐┌────┐┌────┐│
│  │ ES_query        │      │ web  ││col ││ kafka││rpt ││dæm ││
│  │ MySQL_aggregate │      │app   ││ect ││ client││eng││ on ││
│  │ Quartz_sched    │      └──────┘└────┘└──────┘└────┘└────┘│
│  └─────────────────┘                                          │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐    │
│  │               daemon (Zookeeper Watcher)              │    │
│  │   Monitors: reportengine,syslogserver,webapp,         │    │
│  │   managercenter,kafkaclient(+5 additional clients)   │    │
│  └──────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```

## 模块分解与角色矩阵

| 模块 | 类型 | 端口 | 主要职责 | 进程独立部署 |
|------|------|------|---------|-------------|
| `common-module` | 库 | N/A | Zookeeper 客户端、Redis 配置、SM4 加密、数据库备份、工具类 | 否 |
| `socket-comm` | 库 | N/A | TCP Socket 客户端/服务端、消息队列、消息处理 | 否 |
| `collection-engine` | 进程 | UDP syslog | Syslog 接收、数据分类、Kafka 生产 | 是 |
| `kafka-client` | 进程 | 无（Kafka 消费者） | Kafka 消费、策略解析、ES/MySQL 存储、定时清理 | 是 |
| `manager-center` | 进程 | TCP 9090 | 消息路由表查询、Socket 消息转发、系统重启指令 | 是 |
| `report-engine` | 进程 | 无（Quartz 定时任务） | ES 查询聚合、MySQL 报表存储、预统计 | 是 |
| `web-app` | 进程 | HTTPS 8443, Socket 9095 | REST API、WebSocket、CAS 认证、Quartz 调度、文件管理 | 是 |
| `daemon` | 进程 | 无 | Zookeeper 监控、进程健康检查、自动重启 | 是 |

## 分技术栈

| 层级 | 技术选型 | 版本 |
|------|---------|------|
| **基础框架** | Spring Boot | 2.5.15 |
| **语言** | Java | 1.8 |
| **ORM** | MyBatis + PageHelper | 3.5.10 / 1.4.5 |
| **连接池** | Druid / HikariCP | 1.2.4 / 内置 |
| **主数据库** | MySQL | 8.2.0 |
| **国密数据库** | DM 达梦 | 8.1.3.62 |
| **消息队列** | Apache Kafka | 3.4.0 |
| **搜索引擎** | Elasticsearch | 7.17.20 |
| **缓存** | Redis (Lettuce) | Spring Data Redis |
| **服务注册** | Apache Zookeeper | 3.8.4 |
| **进程通信** | 原始 TCP Socket | 自研 |
| **任务调度** | Quartz (JDBC) | Spring Boot Quartz |
| **API 文档** | Swagger 3 (springfox) | 3.0.0 |
| **认证** | CAS | 3.2.1 |
| **加密** | BouncyCastle (SM2/SM4), RSA | 1.78 |
| **构建** | Maven + ProGuard + AppAssembler | 7.2.0-beta2 |

## 存储拓扑

| 存储 | 用途 | 索引/表名 |
|------|------|-----------|
| **MySQL** `aas_simp` | 用户管理、配置、Quartz 任务、路由表、报表数据 | 多张业务表 + `message_routing` |
| **Elasticsearch** | 审计日志存储、检索 | `aassimp-auditlog`, `aassimp-apiauditlog`, `aassimp-dbstatus` |
| **Kafka** | 消息总线 | `aas-audit-log`, `aas-db-info`, `aas-flow-info`, `aas-sens-type`, `aas-dev-alarm`, `aas-api-audit-log`, `aas-classified-list`, `aas-p-log`, `sdm-*` |
| **Redis** | 缓存层 | 本地缓存、会话 |
| **Zookeeper** `/services` | 服务注册与心跳 | 各服务名节点 |

## 数据流

```
安全设备 →(Syslog UDP)→ collection-engine →(Kafka)→ kafka-client →(ES/MySQL)→ report-engine →(MySQL)→ web-app
                                                                            ↓
                                                                          web-app 直接查询 ES
                                                                          
所有进程 →(Socket)→ manager-center ←(Socket)← 所有进程  [双向路由]

所有进程 →(Zookeeper)→ Zookeeper ←(Watch)← daemon  [服务发现与监控]
```

## 子模块角色

### collection-engine 内部
- `NettyUdpServer` — UDP 服务启动
- `NettySyslogService` — Syslog 数据解析与分类
- `NettyKafkaProduce` — Kafka 消息生产

### kafka-client 内部
- `ConsumerListener` — Kafka 消息监听
- `Process*Record` — 各类型数据处理
- `DataAnalysisStrategy` + 策略实现 — 数据解析策略模式
- `DataInToEs` / `DataInToMysql` — 存储写入
- `SaticScheduleTask` — 定时清理

### manager-center 内部
- `ServerSocketService` — TCP 服务器
- `ManagementCenterMessHandler` — 消息路由与转发
- `MessageRoutingMapper` — 路由表查询

### report-engine 内部
- `ReportStats` / `ReportStatsDaily` — 报表统计
- `ReportPretreatment` — 数据预处理
- `DailyStatistics` — 每日统计
- `SearchAuditLog` — 审计日志搜索

### web-app 内部
- 70+ Controllers — REST API 端点
- `WebsocketConfig` — WebSocket 推送
- `QuartzConfig` — 定时调度
- `SwaggerConfig` — API 文档
- `SecurityConfig` — 安全配置
