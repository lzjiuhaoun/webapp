# SYSTEM_ARCHITECTURE.md — 系统架构

## 架构概览

WebApp 是 DB安全综合管理平台的 Web 层，采用 Spring Boot 单体架构，按业务域划分为多个模块。系统通过 REST API 对外服务，通过 Socket 和 Kafka 与兄弟进程通信。

```
┌──────────────────────────────────────────────────────────────┐
│                        前端 (Browser)                        │
└──────────────────────┬───────────────────────────────────────┘
                       │ HTTPS (port 8443)
┌──────────────────────▼───────────────────────────────────────┐
│                     WebApp (本模块)                           │
│  ┌─────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐   │
│  │  auth   │  │  system  │  │ db-assets│  │  device    │   │
│  └─────────┘  └──────────┘  └──────────┘  └────────────┘   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐  │
│  │  risk    │  │situation │  │sensdata  │  │  strategy  │  │
│  └──────────┘  └──────────┘  └──────────┘  └────────────┘  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐  │
│  │ config   │  │  server  │  │  report  │  │ filedata   │  │
│  └──────────┘  └──────────┘  └──────────┘  └────────────┘  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐  │
│  │instruction│ │  backup  │  │  audit   │  │ predict    │  │
│  └──────────┘  └──────────┘  └──────────┘  └────────────┘  │
│  ┌──────────┐  ┌──────────┐                                │
│  │dm-engine │  │ support  │                                │
│  └──────────┘  └──────────┘                                │
└──┬───────────┬───────────┬───────────┬──────────────────────┘
   │           │           │           │
   ▼           ▼           ▼           ▼
┌───────┐ ┌────────┐ ┌────────┐  ┌──────────────┐
│ MySQL │ │  ES    │ │ Kafka  │  │ Manager-Center│
│       │ │  7.x   │ │ 9092   │  │  (port 9090) │
└───────┘ └────────┘ └────────┘  └──────────────┘
```

## 模块分解

### 认证与系统层

| 模块 | 职责 | 关键接口 |
|------|------|---------|
| auth | 用户认证、登录、权限控制 | `/login`, `/user`, CAS SSO |
| system | 系统运维管理 | `/system-*`, `/license`, `/manager-port` |

### 资产管理层

| 模块 | 职责 | 关键接口 |
|------|------|---------|
| db-assets | 数据库资产全生命周期管理 | `/database-assets`, `/assets-inventory`, `/dbasset-classify` |
| device | 安全设备配置与监控 | `/cm`, `/dm`, `/vs`, `/safety-devices` |
| filedata | 文件型数据源管理 | `/filedata-*`, `/file-directory` |

### 安全分析层

| 模块 | 职责 | 关键接口 |
|------|------|---------|
| risk | 多维度风险分析 | `/risk-*` |
| situation | 态势感知与展示 | `/overall-situation`, `/risk-situation`, `/health-situation` |
| sensdata | 敏感数据识别与管理 | `/sensdata-*`, `/dictionary` |
| predict | 态势预测与安全评估 | `/predict-*`, `/assessment` |

### 策略管控层

| 模块 | 职责 | 关键接口 |
|------|------|---------|
| strategy | 脱敏/水印/审计/API策略 | `/api`, `/audit`, `/desens`, `/waterRule` |
| config | 基础安全配置 | `/config-*`, `/situation` |
| audit | 审计规则与策略 | `/audit-rule`, `/audit-strategy` |

### 支撑服务层

| 模块 | 职责 | 关键接口 |
|------|------|---------|
| server | 通知渠道与协议配置 | `/internet-server` |
| report | 报表与BI任务 | `/report-manager`, `/bi-task` |
| backup | 数据备份管理 | `/backup-policy`, `/backup-history` |
| instruction | 设备指令下发 | `/instruction` |
| support | 日志、搜索、拓扑、WebSocket等 | `/log`, `/search`, `/top`, WebSocket |
| dm-engine | 数据脱敏算法引擎 | 内部调用 |

## 存储拓扑

| 存储 | 用途 |
|------|------|
| MySQL (port 23306) | 主业务数据库：用户、配置、资产、策略、任务等 |
| Elasticsearch 7.x (port 9200) | 日志检索、事件检索、敏感数据检索 |
| Kafka (port 9092) | 数据采集事件分发、告警消息 |
| Quartz JDBC | 定时任务持久化（表在 MySQL 中） |
| EhCache | 本地缓存 |

## 子模块角色矩阵

| 模块 | 数据生产者 | 数据消费者 | 事件发布者 | 事件订阅者 |
|------|-----------|-----------|-----------|-----------|
| auth | 用户信息、会话 | — | 登录事件 | — |
| system | 系统配置、告警 | auth, device | 系统告警 | Kafka |
| db-assets | 资产数据、盘点结果 | risk, situation, sensdata | 资产变更 | Kafka |
| device | 设备数据、告警历史 | risk, situation | 设备告警 | Kafka |
| risk | 风险统计 | situation, report | — | db-assets, device |
| situation | 态势展示数据 | report | — | risk, db-assets, device |
| sensdata | 敏感数据发现结果 | risk, report | 发现事件 | Kafka |
| strategy | 策略配置 | device, dm-engine | 策略变更 | — |
| config | 基础配置 | 全模块 | — | — |
| server | 通知配置 | — | — | system |
| report | 报表数据 | — | — | situation, risk |
| instruction | 指令记录 | — | 指令事件 | device |

## 技术分层

```
┌─────────────────────────────────┐
│  Controller  (@RestController)  │  ← HTTP端点层
├─────────────────────────────────┤
│  Service  (Interface + Impl)    │  ← 业务逻辑层
├─────────────────────────────────┤
│  DAO / Mapper  (@Mapper)        │  ← 数据访问层
├─────────────────────────────────┤
│  MyBatis XML Mapper             │  ← SQL映射层
├─────────────────────────────────┤
│  Model / Entity / DTO           │  ← 数据模型层
├─────────────────────────────────┤
│  Utility / Thread / Quartz      │  ← 工具与调度层
└─────────────────────────────────┘
```
