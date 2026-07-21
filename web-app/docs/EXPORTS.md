# EXPORTS.md — web-app 出口契约

> 版本: 1.0.0
> 维护: web-app 模块负责人

## REST API 端点

### 认证相关

| Controller | Base Path | 关键接口 |
|-----------|-----------|---------|
| LoginController | /login | POST /user-login, /cas-login, /sso-user-login, /getToken; GET /check-code |
| UserController | /user | PUT /password, /user-info; POST/GET /user-info |

### 数据检索

| Controller | Base Path | 关键接口 |
|-----------|-----------|---------|
| SearchController | /data-retrieval | POST /log-data-audit, /event-drilling-auditlog; GET /audit-log-detail |
| OemSearchController | /oem-retrieval | POST /log-sens-trace, /reverse-log-sens-trace |

### 报表统计

| Controller | Base Path | 关键接口 |
|-----------|-----------|---------|
| BiTaskController | /statistical-report | POST /task-add, /task-result-export; GET /task-query, /task-result-query |
| ItaTaskController | /ita | POST/PUT/GET 任务管理 CRUD |
| ReportExportController | /export | 报表导出 |

### 数据库资产

| Controller | Base Path | 关键接口 |
|-----------|-----------|---------|
| DbTaskDiscoveryController | /database-discover | 任务发现和扫描 |
| DbAssetsController | /database-asset | 资产 CRUD |
| DbJudgeController | /assets-judge | 资产判定 |
| DbClassificationController | /database-classification | 数据分类 |

### 风险态势

| Controller | Base Path | 关键接口 |
|-----------|-----------|---------|
| SituationPredictController | /situation-predict | GET /safety-predict, /flow-predict |
| SituationAssessmentController | /situation-assessment | 态势评估 |
| RiskSituationController | /data/risk-situation | 风险态势 |
| OverallSituationController | /data/overall-situation | 整体态势 |
| OperationRiskController | /risk/operation-risk | 操作风险 |
| DataAccessRiskController | /risk/data-access-risk | 数据访问风险 |
| AttackRiskController | /risk/attack-risk | 攻击风险 |

### 配置管理

| Controller | Base Path | 说明 |
|-----------|-----------|------|
| WarnStrategyConfController | /warn-strategy-conf | 告警策略 |
| SecurityRulesController | /security-rules | 安全规则 |
| AuditStrategyConfigController | /audit-strategy-config | 审计策略 |
| LicenseController | /license | 许可证管理 |
| OrganizationConfigController | /org-config | 组织架构 |
| LogicAreaController | /logic-area | 逻辑区域 |

### 服务器与通知

| Controller | Base Path | 说明 |
|-----------|-----------|------|
| WebsocketConfigController | /websocket-config | WebSocket 配置 |
| SyslogConfigController | /syslog-config | Syslog 配置 |
| MailConfigController | /mail-config | 邮件配置 |
| SmsConfigController | /sms-config | 短信配置 |
| DingTalkConfigController | /dingtalk-config | 钉钉配置 |

### 敏感数据

| Controller | Base Path | 说明 |
|-----------|-----------|------|
| SensDataSummaryController | /sens-data-summary | 敏感数据汇总 |
| SensitiveTypeController | /sens-type | 敏感类型 |
| SensDataTaskController | /sens-data-task | 敏感数据任务 |

### 系统维护

| Controller | Base Path | 说明 |
|-----------|-----------|------|
| SystemUpgradeController | /system-upgrade | 系统升级 |
| BackupPolicyController | /sys_maintenance | 备份策略 |
| BackupHistoryController | /backup-history | 备份历史 |
| DeployController | /deploy | 部署管理 |

### 其他

| Controller | Base Path | 说明 |
|-----------|-----------|------|
| ScreenMonitorController | /system-monitor-wall | 大屏监控 |
| InstructionController | /instruction | 指令下发 |
| TopGraphController | /topology | 拓扑图 |
| FileSourceConfigController | /file-source-config | 文件源配置 |

## WebSocket 端点

- WebSocket 推送：实时消息推送（审计日志、告警等）

## Socket 端点

- TCP 9095：接收 manager-center 转发的 Socket 消息

## 服务注册

- Zookeeper 路径: `/services/webapp`
- 注册数据: `{host}:{port}`
- 节点类型: `EPHEMERAL`（进程退出自动删除）
