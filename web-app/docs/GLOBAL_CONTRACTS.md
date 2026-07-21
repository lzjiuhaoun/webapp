# GLOBAL_CONTRACTS.md — 规范条约

## 跨模块通信契约

本文档定义 WebApp 各联邦模块之间的硬性通信约定。任何影响他模块的变更须先更新本文档并提升版本号。

**版本：** v1.0.0

---

## 1. 全局唯一标识符规范

| 标识符类型 | 格式 | 示例 | 负责模块 |
|-----------|------|------|---------|
| 用户ID | 字符串 | `U001` | auth |
| 数据库资产ID | 字符串 | `DA001` | db-assets |
| 设备ID | 字符串 | `DEV001` | device |
| 任务ID | UUID | `550e8400-e29b...` | 各模块 |
| 策略ID | 字符串 | `ST001` | strategy |
| 告警ID | 字符串 | `AL001` | system |
| 敏感数据类型ID | 字符串 | `ST001` | sensdata |

## 2. 模块间边界承诺

### 2.1 auth → 全模块

- **承诺提供**：当前登录用户身份信息（通过 Session / Token）
- **承诺提供**：用户角色和权限列表
- **不提供**：用户密码明文
- **接口位置**：`auth/EXPORTS.md`

### 2.2 config → 全模块

- **承诺提供**：组织架构树、逻辑区域划分、物理位置信息
- **承诺提供**：安全模型定义、安全规则定义
- **承诺提供**：嫌疑IP/域名/URL列表
- **接口位置**：`config/EXPORTS.md`

### 2.3 db-assets → risk, situation, sensdata, report

- **承诺提供**：数据库资产列表及属性
- **承诺提供**：资产分类定级结果
- **承诺提供**：资产发现任务状态与结果
- **承诺提供**：数据表结构信息（库名、表名、字段名、字段类型）
- **接口位置**：`db-assets/EXPORTS.md`

### 2.4 device → risk, situation, system

- **承诺提供**：设备列表及运行状态
- **承诺提供**：设备告警历史
- **承诺提供**：脱敏任务（DM设备）结果
- **承诺提供**：数据库防火墙（VS设备）拦截数据
- **接口位置**：`device/EXPORTS.md`

### 2.5 sensdata → risk, situation, report, strategy

- **承诺提供**：敏感数据发现任务结果
- **承诺提供**：敏感数据统计汇总
- **承诺提供**：敏感数据类型字典
- **接口位置**：`sensdata/EXPORTS.md`

### 2.6 risk → situation, report

- **承诺提供**：攻击风险统计数据
- **承诺提供**：漏洞风险统计数据
- **承诺提供**：运维风险统计数据
- **承诺提供**：数据访问风险统计数据
- **接口位置**：`risk/EXPORTS.md`

### 2.7 strategy → device, dm-engine

- **承诺提供**：脱敏策略配置
- **承诺提供**：水印规则配置
- **承诺提供**：审计策略配置
- **承诺提供**：API策略配置
- **接口位置**：`strategy/EXPORTS.md`

### 2.8 strategy → instruction → device

- **策略变更** 时，strategy 模块通过 instruction 模块下发指令到对应设备
- instruction 模块负责指令封装、下发、ACK确认、重试
- **接口位置**：`strategy/EXPORTS.md` → `instruction/EXPORTS.md`

### 2.9 server → system

- **承诺提供**：告警通知渠道（邮件、短信、钉钉、SNMP、Syslog）
- **承诺提供**：通知发送能力
- **接口位置**：`server/EXPORTS.md`

### 2.10 dm-engine → sensdata, strategy

- **承诺提供**：脱敏算法执行能力（检查、映射、随机、降维、可逆、分区）
- **承诺提供**：敏感数据类型识别能力
- **接口位置**：`dm-engine/EXPORTS.md`

### 2.11 system → 全模块

- **承诺提供**：系统运行状态信息
- **承诺提供**：系统告警管理能力
- **承诺提供**：License 校验能力
- **接口位置**：`system/EXPORTS.md`

## 3. 模块间通信方式

| 通信方式 | 适用场景 | 示例 |
|---------|---------|------|
| 方法调用（Service 注入） | 同进程模块间同步调用 | strategy 调用 dm-engine 执行脱敏 |
| Quartz 定时任务 | 周期性数据同步 | 资产盘点任务、统计数据汇总 |
| @Scheduled 线程 | 持续性后台任务 | 实时分析、离线分析 |
| Kafka | 异步事件分发 | 数据采集事件、告警事件 |
| Socket (socket-comm) | 跨进程通信 | 向 manager-center 发送指令 |
| WebSocket | 实时推送 | 前端实时告警推送 |
| Elasticsearch | 日志和事件检索 | 审计日志搜索、事件检索 |

## 4. 数据格式约定

| 约定项 | 规则 |
|-------|------|
| JSON 序列化 | 统一使用 Jackson，日期格式 `yyyy-MM-dd HH:mm:ss` |
| 分页响应 | PageHelper 标准格式：`pageNum`, `pageSize`, `total`, `list` |
| 统一返回体 | 使用模块 `common-module` 定义的 `AjaxResult` |
| 错误码 | 各模块独立维护，不跨模块共享错误码 |
| 时区 | 统一使用服务器时区（Asia/Shanghai） |

## 5. 版本号约定

各模块 `EXPORTS.md` 的接口版本号格式：`MAJOR.MINOR.PATCH`

- MAJOR：不兼容变更（如字段删除、类型变更）
- MINOR：向后兼容的新增（如新增字段、新增端点）
- PATCH：内部修复（不影响接口签名）
