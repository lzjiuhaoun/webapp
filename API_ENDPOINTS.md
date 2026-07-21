# API_ENDPOINTS.md — REST API 接口文档

## 概述

web-app 模块提供 70+ REST API 控制器，所有 Controller 继承 BaseController，默认启用 @CrossOrigin 跨域。所有响应遵循 WebResult 模式，包含 code、message、data、logs 字段。

---

## 1. 认证与用户管理

### LoginController (`/login`)

| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/check-code` | 生成登录验证码 | `retValue` (query) | WebResult |
| GET | `/msg-check-code` | 发送短信验证码 | `mobilePhoneNumber`, `retValue` | WebResult |
| POST | `/user-logout` | 用户登出 | Header: `Authorization` token | WebResult |
| POST | `/system-lock-screen` | 锁定屏幕 | `lockPwd`, Header: `Authorization` | WebResult |
| POST | `/unicom-user-login` | 联通用户登录 | `LoginInfo` (body) | WebResult |
| POST | `/sso-user-login` | SSO 登录 | `userInfo` (query string, encrypted) | WebResult |
| POST | `/unit-login` | 能力单元登录 | `key` (1-8), `ip` | WebResult |
| POST | `/user-login` | 标准用户登录 | `LoginInfo` (body) | WebResult |
| GET/POST | `/cas-nmxy-login` | CAS SSO (内蒙古商贸职业学院) | HttpServletRequest | WebResult |
| POST | `/cas-login` | CAS 登录 | HttpServletRequest | WebResult |
| POST | `/unit-login-sso` | 能力单元 SSO 登录 | `devUuId` (query) | WebResult |
| POST | `/getToken` | 第三方系统获取 token | `TokenQueryDTO` (body) | WebResult |

### UserController (`/user`)

| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| PUT | `/password` | 修改用户密码 | `oldPassword`, `newPassword`, Header: `Authorization` | WebResult |
| POST | `/delete/user-info` | 删除用户 | `List<Integer>` userIds (body) | WebResult |
| POST | `/user-info` | 添加用户 | `User` (body) | WebResult |
| GET | `/password` | 重置密码 | `userId`, `curPassword`, `password` | WebResult |
| PUT | `/user-info` | 修改用户信息 | `User` (body) | WebResult |
| GET | `/all-user-info` | 获取所有用户 (管理员) | `User` (query), Header: `Authorization` | WebResult |
| GET | `/user-info` | 获取用户信息 | `userId`, Header: `Authorization` | WebResult |
| PUT | `/user-lock-state` | 锁定/解锁用户 | `User` (body) | WebResult |
| GET | `/user-popedom` | 查询用户权限 | `userId` | WebResult |
| GET | `/user-info-by-token` | 通过 token 获取用户信息 | Header: `Authorization` | WebResult |
| PUT | `/twoFactorLogin` | 修改登录因子 | `Map<String,Object>` (body) | WebResult |

---

## 2. 系统配置

### SystemInfoController (`/system-info`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/get-system-info` | 获取系统信息 | WebResult |
| PUT | `/update-system-info` | 更新系统信息 | WebResult |
| POST | `/reset-system-info` | 重置系统信息 | WebResult |

### SystemAlarmController (`/system-alarm`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/alarm-config` | 获取告警配置 | WebResult |
| PUT | `/alarm-config` | 更新告警配置 | WebResult |
| POST | `/alarm-config` | 添加告警配置 | WebResult |
| DELETE | `/alarm-config/{id}` | 删除告警配置 | WebResult |

### SystemUpgradeController (`/system-upgrade`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/upgrade-info` | 获取升级信息 | WebResult |
| POST | `/upgrade` | 开始升级 | WebResult |

### SystemTimeController (`/system-time`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/time-config` | 获取时间配置 | WebResult |
| PUT | `/time-config` | 更新时间配置 | WebResult |

### SystemThemeController (`/system-theme`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/theme` | 获取主题 | WebResult |
| PUT | `/theme` | 更新主题 | WebResult |

### SystemMaintenanceController (`/system-maintenance`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/maintenance-info` | 获取维护信息 | WebResult |
| PUT | `/maintenance-info` | 更新维护信息 | WebResult |

### SecurityConfigController (`/security-config`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/security-config` | 获取安全配置 | WebResult |
| PUT | `/security-config` | 更新安全配置 | WebResult |

### LicenseController (`/license`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/license-info` | 获取许可证信息 | WebResult |
| POST | `/check-license` | 检查许可证 | WebResult |

### DeployController (`/deploy`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/deploy-info` | 获取部署信息 | WebResult |

### LoginIpConfigController (`/login-ip-config`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/config` | 获取登录 IP 配置 | WebResult |
| PUT | `/config` | 更新登录 IP 配置 | WebResult |

---

## 3. 服务器配置 (全部在 `/internet-server`)

### SyslogConfigController
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/syslog-server-config` | 列出所有 Syslog 配置 | WebResult |
| DELETE | `/syslog-server-config/{syslogConfigId}` | 删除 Syslog 配置 | WebResult |
| POST | `/syslog-server-config` | 添加 Syslog 配置 | WebResult |
| PUT | `/syslog-server-config` | 更新 Syslog 配置 | WebResult |
| POST | `/syslog-config-test` | 测试 Syslog 连接 | WebResult |

### SnmpConfigController
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/snmp-server-config` | 列出所有 SNMP 配置 | WebResult |
| DELETE | `/snmp-server-config/{snmpConfigId}` | 删除 SNMP 配置 | WebResult |
| POST | `/snmp-server-config` | 添加 SNMP 配置 | WebResult |
| PUT | `/snmp-server-config` | 更新 SNMP 配置 | WebResult |
| POST | `/snmp-config-test` | 测试 SNMP 连接 | WebResult |
| POST | `/dev-snmp-test` | 测试 SNMP 性能 | WebResult |

### MailConfigController
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/mail-server-config` | 列出所有邮件配置 | WebResult |
| PUT | `/mail-server-status` | 更新邮件状态 | WebResult |
| POST | `/mail-server-config` | 添加/更新邮件配置 (body: list) | WebResult |
| POST | `/mail-config-test` | 测试邮件连接 | WebResult |
| DELETE | `/mail-server-config/{mailConfigId}` | 删除邮件配置 | WebResult |

### DingTalkConfigController
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/ding-server-config` | 列出所有钉钉配置 | WebResult |
| PUT | `/ding-server-status` | 更新钉钉状态 | WebResult |
| POST | `/ding-server-config` | 添加钉钉配置 | WebResult |
| DELETE | `/ding-server-config/{id}` | 删除钉钉配置 | WebResult |

### FtpConfigController
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/ftp-server-config` | 列出所有 FTP 配置 | WebResult |
| DELETE | `/ftp-server-config/{ftpConfigId}` | 删除 FTP 配置 | WebResult |
| POST | `/ftp-server-config` | 添加 FTP 配置 | WebResult |
| PUT | `/ftp-server-config` | 更新 FTP 配置 | WebResult |
| POST | `/ftp-config-test` | 测试 FTP 连接 | WebResult |

### SmsConfigController (`/sms-server`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/sms-platform-config` | 列出短信平台配置 | WebResult |
| DELETE | `/sms-config/{smsConfigId}` | 删除短信配置 | WebResult |
| POST | `/sms-config` | 添加短信配置 | WebResult |
| PUT | `/sms-config` | 更新短信配置 | WebResult |

### SftpConfigController
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| POST | `/sftp` | 添加 SFTP 配置 | WebResult |
| PUT | `/sftp/update` | 更新 SFTP 配置 | WebResult |
| DELETE | `/sftp` | 删除 SFTP 配置 | WebResult |
| GET | `/sftp/getOne` | 获取 SFTP 配置 | WebResult |
| GET | `/sftp/page` | 列出 SFTP 配置 (分页) | WebResult |
| GET | `/sftp/getAll` | 获取所有 SFTP 配置 | WebResult |
| POST | `/sftp/test` | 测试 SFTP 连接 | WebResult |
| POST | `/sftp/start` | 启动 SFTP 服务 | WebResult |
| POST | `/sftp/stop` | 停止 SFTP 服务 | WebResult |

### WebsocketConfigController
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| POST | `/websocket` | 添加 WebSocket 配置 | WebResult |
| PUT | `/websocket/update` | 更新 WebSocket 配置 | WebResult |
| DELETE | `/websocket` | 删除 WebSocket 配置 | WebResult |
| GET | `/websocket/getOne` | 获取 WebSocket 配置 | WebResult |
| GET | `/websocket/getAll` | 获取所有 WebSocket 配置 | WebResult |
| GET | `/websocket/page` | 列出 WebSocket 配置 (分页) | WebResult |
| POST | `/websocket/test` | 测试 WebSocket 连接 | WebResult |
| POST | `/websocket/start` | 启动 WebSocket 服务 | WebResult |
| POST | `/websocket/stop` | 停止 WebSocket 服务 | WebResult |

### MsgPlatformController
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/msg-platform-config` | 列出消息平台配置 | WebResult |
| DELETE | `/msg-platform-config/{msgConfigId}` | 删除消息配置 | WebResult |
| POST | `/msg-platform-config` | 添加消息配置 | WebResult |
| PUT | `/msg-platform-config` | 更新消息配置 | WebResult |

---

## 4. 配置管理

### SecurityModelsController (`/security-model`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/model-config` | 列出所有安全模型 | `SecurityModels` (query) | WebResult |
| POST | `/delete/model-config` | 删除安全模型 | `List<Integer>` modelIds (body) | WebResult |
| POST | `/model-config` | 添加安全模型 | `SecurityModels` (body) | WebResult |
| PUT | `/model-config` | 更新安全模型 | `SecurityModels` (body) | WebResult |
| PUT | `/model-strategy-batch` | 批量更新策略 | `List<SecurityModels>` (body) | WebResult |
| GET | `/model-info` | 获取模型详情 | `modelId` (query) | WebResult |

### SecurityRulesController (`/security-rule`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/rule-config` | 列出所有安全规则 | `SecurityRules` (query) | WebResult |
| POST | `/delete/rule-config` | 删除安全规则 | `List<Integer>` ruleIds (body) | WebResult |
| POST | `/rule-config` | 添加安全规则 | `SecurityRules` (body) | WebResult |
| PUT | `/rule-config` | 更新安全规则 | `SecurityRules` (body) | WebResult |
| GET | `/rule-info` | 获取规则详情 | `ruleId` (query) | WebResult |
| GET | `/data-field` | 获取所有日志类型和字段 | - | WebResult |
| GET | `/rules` | 获取所有规则名称 | - | WebResult |

### WarnStrategyConfController (`/alarm-strategy`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| POST | `/conf` | 添加告警策略 | `WarnStrategyConf` (body) | WebResult |
| PUT | `/conf` | 更新告警策略 | `WarnStrategyConf` (body) | WebResult |
| POST | `/delete/conf` | 删除告警策略 | `List<Integer>` ids (body) | WebResult |
| GET | `/conf` | 列出告警策略 | `WarnStrategyConf` (query) | WebResult |
| GET | `/test` | 测试告警通知 | `WarnStrategyConf` (query) | WebResult |
| GET | `/get-reciver` | 获取通知接收者 | - | WebResult |
| GET | `/strategy-name` | 获取告警策略名称 | - | WebResult |

### OrganizationConfigController (`/orgnization-structure`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| POST | `/structure-info` | 添加组织 | `OrganizationConfig` (body) | WebResult |
| PUT | `/structure-info` | 更新组织 | `OrganizationConfig` (body) | WebResult |
| DELETE | `/structure-info/{departId}` | 删除组织 | `departId` (path) | WebResult |
| GET | `/structure-info` | 查询组织 | `OrganizationConfig` (query) | WebResult |
| GET | `/query-parent-info` | 获取顶级组织 | - | WebResult |
| GET | `/structure-all` | 获取所有组织 (下拉) | - | WebResult |
| PUT | `/company-name` | 更新公司名称 | `SysConf` (body) | WebResult |
| GET | `/dept-dbs` | 获取部门资产信息 | - | WebResult |
| GET | `/excel-export` | 导出组织配置 | query params | WebResult |
| POST | `/excel-upload` | 上传组织 Excel | `MultipartFile` | WebResult |
| POST | `/excel-import` | 导入组织配置 | - | WebResult |
| GET | `/excel-download` | 下载模板 | - | WebResult |

### ManagerConfigController (`/manager-config`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/manager` | 列出所有经理 | `ManagerConfig` (query) | WebResult |
| POST | `/delete/manager` | 删除经理 | `List<Integer>` ids (body) | WebResult |
| POST | `/manager` | 添加经理 | `ManagerConfig` (body) | WebResult |
| PUT | `/manager` | 更新经理 | `ManagerConfig` (body) | WebResult |
| GET | `/manager-name` | 获取所有经理名称 | - | WebResult |
| GET | `/name` | 通过 ID 获取名称 | `managerId` (query) | WebResult |
| GET | `/excel-export` | 导出经理配置 | query params | WebResult |
| POST | `/excel-upload` | 上传经理 Excel | `MultipartFile` | WebResult |
| POST | `/excel-import` | 导入经理配置 | - | WebResult |
| GET | `/excel-download` | 下载模板 | - | WebResult |

### PhysicalPosController (`/physical-pos`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/pos-data` | 列出所有物理位置 | `PhysicalAddrConfig` (query) | WebResult |
| POST | `/delete/pos-data` | 删除物理位置 | `List<Integer>` ids (body) | WebResult |
| POST | `/pos-data` | 添加物理位置 | `PhysicalAddrConfig` (body) | WebResult |
| PUT | `/pos-data` | 更新物理位置 | `PhysicalAddrConfig` (body) | WebResult |
| GET | `/assets` | 获取应用资产数据 | - | WebResult |
| GET | `/excel-export` | 导出物理配置 | query params | WebResult |
| GET | `/excel-download` | 下载模板 | - | WebResult |
| POST | `/excel-upload` | 上传物理 Excel | `MultipartFile` | WebResult |
| POST | `/excel-import` | 导入物理配置 | - | WebResult |
| GET | `/pos-name` | 获取物理位置名称 | - | WebResult |

### LogicAreaController (`/business-logic-area`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/area-data` | 列出所有逻辑区域 | `LogicAreaConfig` (query) | WebResult |
| POST | `/delete/area-data` | 删除逻辑区域 | `List<Integer>` ids (body) | WebResult |
| POST | `/area-data` | 添加逻辑区域 | `LogicAreaConfig` (body) | WebResult |
| PUT | `/area-data` | 更新逻辑区域 | `LogicAreaConfig` (body) | WebResult |
| GET | `/excel-export` | 导出逻辑区域配置 | query params | WebResult |
| GET | `/excel-download` | 下载模板 | - | WebResult |
| POST | `/excel-upload` | 上传逻辑 Excel | `MultipartFile` | WebResult |
| POST | `/excel-import` | 导入逻辑区域配置 | - | WebResult |
| GET | `/area-name` | 获取逻辑区域名称 | - | WebResult |
| GET | `/one-area` | 获取顶级区域名称 | - | WebResult |

### ProvinceCityController (`/province-city`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/province` | 获取所有省份 | - | WebResult |
| GET | `/city` | 获取省份下的城市 | `provinceId` (query) | WebResult |
| GET | `/area` | 获取城市下的区县 | `cityId` (query) | WebResult |

### DataReportingConfController (`/data_reporting`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/reporting_conf_list` | 列出所有数据上报配置 | `DataReportingConf` (query) | WebResult |
| POST | `/delete/data_reporting_conf` | 删除配置 | `List<Integer>` ids (body) | WebResult |
| POST | `/data_reporting_conf` | 添加数据上报配置 | `DataReportingConf` (body) | WebResult |
| PUT | `/data_reporting_conf` | 更新数据上报配置 | `DataReportingConf` (body) | WebResult |
| GET | `/data_reporting_conf` | 获取配置详情 | `dataReportingConfId` (query) | WebResult |
| POST | `/data_reporting_conf_syslog_test` | 测试 Syslog 连接 | `DataReportingConf` (body) | WebResult |
| POST | `/test_data_reporting_syslog_sendmsg` | 测试 Syslog 消息发送 | `dataReportingConfId` (body) | WebResult |

### SuspiciousListController (`/suspicious-list`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/suspicious` | 列出所有可疑列表 | `SuspiciousList` (query) | WebResult |
| POST | `/suspicious` | 添加可疑列表 | `SuspiciousList` (body) | WebResult |
| POST | `/delete/suspicious` | 删除可疑列表 | `List<Integer>` ids (body) | WebResult |
| PUT | `/suspicious` | 更新可疑列表 | `SuspiciousList` (body) | WebResult |
| GET | `/suspicious-detail` | 获取可疑详情 | `suspiciousId` (query) | WebResult |
| PUT | `/audit/suspicious` | 批量审核可疑列表 | `Map<String,Object>` (body) | WebResult |
| GET | `/excel-export` | 导出可疑列表 | query params | WebResult |
| POST | `/excel-upload` | 上传可疑 Excel | `MultipartFile` | WebResult |
| POST | `/excel-import` | 导入可疑列表配置 | - | WebResult |
| GET | `/excel-download` | 下载模板 | - | WebResult |

### AccessListController (`/alert-access-list`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/alert-access-list` | 黑白名单分页列表 | `pageNo`, `pageSize`, `keyword`, `type` (query) | `Map` (pageNo/pageSize/total/list) |
| GET | `/alert-access-list/detail` | 黑白名单详情（含用户列表） | `id` (query) | `AccessList` |
| GET | `/alert-access-list/names` | 获取所有启用的黑白名单名称（供规则引用） | `type` (query, optional) | `List<AccessList>` |
| POST | `/alert-access-list` | 新增黑白名单 | `AccessList` (body, 含users) | `Integer` (id) |
| PUT | `/alert-access-list` | 编辑黑白名单 | `AccessList` (body, 含users) | `Integer` (id) |
| POST | `/alert-access-list/delete` | 删除黑白名单（批量） | `List<Integer>` ids (body) | `Integer` (删除数) |
| PUT | `/alert-access-list/status` | 切换启用/禁用状态 | `{"id": 1}` (body) | `Integer` (影响行数) |

### SuspiciousGroupController (`/suspicious-group`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| GET | `/group` | 列出可疑组 | `SuspiciousGroup` (query) | WebResult |
| POST | `/group` | 添加可疑组 | `SuspiciousGroup` (body) | WebResult |
| POST | `/delete/group` | 删除组 | `List<Integer>` ids (body) | WebResult |
| PUT | `/group` | 更新可疑组 | `SuspiciousGroup` (body) | WebResult |
| GET | `/suspicious` | 获取组内的可疑列表 | query params | WebResult |
| POST | `/delete/suspicious` | 从组中移除可疑列表 | `Map<String,Object>` (body) | WebResult |
| GET | `/suspiciouslist` | 获取未链接的可疑列表 | query params | WebResult |
| POST | `/add/suspicious` | 添加可疑列表到组 | `Map<String,Object>` (body) | WebResult |

---

## 5. 数据与统计

### SearchController (`/data-retrieval`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| POST | `/log-data-audit` | 搜索审计日志 | `AuditRecordCondition` (body) | WebResult |
| GET | `/safety-devices` | 获取能力单元列表 | `logTypeStr` (query) | WebResult |
| GET | `/oper-type` | 获取操作类型 | - | WebResult |
| GET | `/audit-log-detail` | 获取审计日志详情 | `AuditRecord` (query) | WebResult |
| POST | `/event-drilling-auditlog` | 钻取审计日志 | query params + `SafetyEvent` (body) | WebResult |

### ApiAuditUrlController (`/api-audit`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| POST | `/log-api-audit` | 搜索 API 审计日志 | `ApiAuditRecordCondition` (body) | WebResult |
| GET | `/url-info` | 查询 API 审计 URL | `ApiAuditUrl` (query) | WebResult |
| GET | `/log-api-dict` | 获取 API 风险组 | `devUuid` (query) | WebResult |
| GET | `/log-api-risk-level` | 获取 API 风险等级 | `devUuid` (query) | WebResult |
| GET | `/log-api-rule` | 获取 API 风险规则 | `devUuid` (query) | WebResult |
| POST | `/log-sens-trace` | 敏感数据流量分析 | `SensQuery` (body) | WebResult |

### EventSearchController (`/event-search`)
| Method | Path | Description | Request Params | Return Type |
|--------|------|-------------|----------------|-------------|
| POST | `/event-search` | 搜索事件 | query params + body | WebResult |
| GET | `/event-detail` | 获取事件详情 | query params | WebResult |

### AssetsOverviewController (`/assets-overview`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/overview` | 获取资产概览 | WebResult |
| GET | `/detail` | 获取资产详情 | WebResult |

### DataFlowOverviewController (`/data-flow-overview`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/overview` | 获取数据流概览 | WebResult |
| GET | `/detail` | 获取数据流详情 | WebResult |

### ModelOverviewController (`/model-overview`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/overview` | 获取模型概览 | WebResult |

### RiskSituationController (`/risk-situation`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/overview` | 获取风险态势概览 | WebResult |

### OverallSituationController (`/overall-situation`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/overview` | 获取整体态势 | WebResult |

---

## 6. 风险管理

### AttackRiskController (`/attack-risk`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/attack-risk` | 列出攻击风险 | WebResult |
| GET | `/attack-detail` | 获取攻击风险详情 | WebResult |

### DataAccessRiskController (`/data-access-risk`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/data-access-risk` | 列出数据访问风险 | WebResult |

### OperationRiskController (`/operation-risk`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/operation-risk` | 列出操作风险 | WebResult |

### OverallRiskController (`/overall-risk`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/overall-risk` | 获取整体风险 | WebResult |

### VulnerabilityRiskController (`/vulnerability-risk`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/vulnerability-risk` | 列出漏洞风险 | WebResult |

---

## 7. 策略管理

### AuditStrategyController (`/audit-strategy`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/audit-strategy` | 列出审计策略 | WebResult |
| POST | `/audit-strategy` | 添加审计策略 | WebResult |
| PUT | `/audit-strategy` | 更新审计策略 | WebResult |
| DELETE | `/audit-strategy/{id}` | 删除审计策略 | WebResult |

### ApiStrategyController (`/api-strategy`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/api-strategy` | 列出 API 策略 | WebResult |
| POST | `/api-strategy` | 添加 API 策略 | WebResult |
| PUT | `/api-strategy` | 更新 API 策略 | WebResult |
| DELETE | `/api-strategy/{id}` | 删除 API 策略 | WebResult |

### DesenRuleGroupController (`/desen-rule-group`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/desen-rule-group` | 列出脱敏规则组 | WebResult |
| POST | `/desen-rule-group` | 添加脱敏规则组 | WebResult |
| PUT | `/desen-rule-group` | 更新脱敏规则组 | WebResult |
| DELETE | `/desen-rule-group/{id}` | 删除脱敏规则组 | WebResult |

### DesensStrategyController (`/desens-strategy`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/desens-strategy` | 列出脱敏策略 | WebResult |
| POST | `/desens-strategy` | 添加脱敏策略 | WebResult |
| PUT | `/desens-strategy` | 更新脱敏策略 | WebResult |
| DELETE | `/desens-strategy/{id}` | 删除脱敏策略 | WebResult |

### WaterRuleController (`/water-rule`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/water-rule` | 列出水印规则 | WebResult |
| POST | `/water-rule` | 添加水印规则 | WebResult |
| PUT | `/water-rule` | 更新水印规则 | WebResult |
| DELETE | `/water-rule/{id}` | 删除水印规则 | WebResult |

### WaterRuleGroupController (`/water-rule-group`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/water-rule-group` | 列出水印规则组 | WebResult |
| POST | `/water-rule-group` | 添加水印规则组 | WebResult |
| PUT | `/water-rule-group` | 更新水印规则组 | WebResult |
| DELETE | `/water-rule-group/{id}` | 删除水印规则组 | WebResult |

---

## 8. 数据维护与分类

### DataMaintainController (`/data-maintenance`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/maintain-info` | 获取维护信息 | WebResult |
| POST | `/maintain` | 开始数据维护 | WebResult |

### DbAssetsController (`/db-assets`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/db-assets` | 列出数据库资产 | WebResult |
| GET | `/db-assets-detail` | 获取数据库资产详情 | WebResult |

### DbAssetsStateController (`/db-assets-state`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/db-assets-state` | 列出数据库资产状态 | WebResult |

### DbClassificationController (`/db-classification`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/db-classification` | 列出数据库分类 | WebResult |

### DbDirectoryController (`/db-directory`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/db-directory` | 列出数据库目录 | WebResult |

### DbInventoryController (`/db-inventory`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/db-inventory` | 列出数据库清单 | WebResult |

### DbJudgeController (`/db-judge`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/db-judge` | 列出数据库判断 | WebResult |

### DbTaskDiscoveryController (`/db-task-discovery`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/db-task-discovery` | 列出发现任务 | WebResult |
| POST | `/db-task-discovery` | 启动发现任务 | WebResult |

### AssetClassifyTaskController (`/asset-classify-task`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/asset-classify-task` | 列出分类任务 | WebResult |
| POST | `/asset-classify-task` | 启动分类任务 | WebResult |

### SensDataTaskController (`/sens-data-task`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/sens-data-task` | 列出敏感数据任务 | WebResult |
| POST | `/sens-data-task` | 启动敏感数据任务 | WebResult |

### SensDataSummaryController (`/sens-data-summary`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/sens-data-summary` | 获取敏感数据汇总 | WebResult |

### SensitiveTypeController (`/sensitive-type`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/sensitive-type` | 列出敏感类型 | WebResult |
| POST | `/sensitive-type` | 添加敏感类型 | WebResult |
| PUT | `/sensitive-type` | 更新敏感类型 | WebResult |
| DELETE | `/sensitive-type/{id}` | 删除敏感类型 | WebResult |

### DictionaryController (`/dictionary`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/dictionary` | 列出字典 | WebResult |
| POST | `/dictionary` | 添加字典 | WebResult |
| PUT | `/dictionary` | 更新字典 | WebResult |
| DELETE | `/dictionary/{id}` | 删除字典 | WebResult |

---

## 9. 设备配置

### CmDevController (`/cm-dev`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/cm-dev` | 列出 CM 设备 | WebResult |
| POST | `/cm-dev` | 添加 CM 设备 | WebResult |
| PUT | `/cm-dev` | 更新 CM 设备 | WebResult |
| DELETE | `/cm-dev/{id}` | 删除 CM 设备 | WebResult |

### DmDevController (`/dm-dev`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/dm-dev` | 列出 DM 设备 | WebResult |
| POST | `/dm-dev` | 添加 DM 设备 | WebResult |
| PUT | `/dm-dev` | 更新 DM 设备 | WebResult |
| DELETE | `/dm-dev/{id}` | 删除 DM 设备 | WebResult |

### VsDevController (`/vs-dev`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/vs-dev` | 列出 VS 设备 | WebResult |
| POST | `/vs-dev` | 添加 VS 设备 | WebResult |
| PUT | `/vs-dev` | 更新 VS 设备 | WebResult |
| DELETE | `/vs-dev/{id}` | 删除 VS 设备 | WebResult |

### SafetyDevConfigController (`/safety-dev-config`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/safety-dev-config` | 列出安全设备配置 | WebResult |
| POST | `/safety-dev-config` | 添加安全设备配置 | WebResult |
| PUT | `/safety-dev-config` | 更新安全设备配置 | WebResult |
| DELETE | `/safety-dev-config/{id}` | 删除安全设备配置 | WebResult |

### DevAlarmHistoryController (`/dev-alarm-history`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/dev-alarm-history` | 列出设备告警历史 | WebResult |

---

## 10. 文件数据管理

### FileDataInfoController (`/file-data-info`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/file-data-info` | 列出文件数据信息 | WebResult |
| GET | `/file-data-info-detail` | 获取文件数据详情 | WebResult |

### FileDataTemplateController (`/file-data-template`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/file-data-template` | 列出文件数据模板 | WebResult |
| POST | `/file-data-template` | 添加文件数据模板 | WebResult |
| PUT | `/file-data-template` | 更新文件数据模板 | WebResult |
| DELETE | `/file-data-template/{id}` | 删除文件数据模板 | WebResult |

### FileDirectoryDetailController (`/file-directory-detail`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/file-directory-detail` | 列出文件目录详情 | WebResult |

### FileSourceConfigController (`/file-source-config`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/file-source-config` | 列出文件源配置 | WebResult |
| POST | `/file-source-config` | 添加文件源配置 | WebResult |
| PUT | `/file-source-config` | 更新文件源配置 | WebResult |
| DELETE | `/file-source-config/{id}` | 删除文件源配置 | WebResult |

---

## 11. 备份管理

### BackupHistoryController (`/backup-history`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/backup-history` | 列出备份历史 | WebResult |

### BackupPolicyController (`/backup-policy`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/backup-policy` | 列出备份策略 | WebResult |
| POST | `/backup-policy` | 添加备份策略 | WebResult |
| PUT | `/backup-policy` | 更新备份策略 | WebResult |
| DELETE | `/backup-policy/{id}` | 删除备份策略 | WebResult |

---

## 12. 态势分析

### HealthSituationController (`/health-situation`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/health-situation` | 获取健康态势 | WebResult |

### SituationConfController (`/situation-conf`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/situation-conf` | 列出态势配置 | WebResult |
| POST | `/situation-conf` | 添加态势配置 | WebResult |
| PUT | `/situation-conf` | 更新态势配置 | WebResult |
| DELETE | `/situation-conf/{id}` | 删除态势配置 | WebResult |

### VulnerabilitySituationController (`/vulnerability-situation`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/vulnerability-situation` | 获取漏洞态势 | WebResult |

---

## 13. 预测

### SituationAssessmentController (`/situation-assessment`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/situation-assessment` | 列出态势评估 | WebResult |
| POST | `/situation-assessment` | 添加态势评估 | WebResult |
| PUT | `/situation-assessment` | 更新态势评估 | WebResult |
| DELETE | `/situation-assessment/{id}` | 删除态势评估 | WebResult |

### SituationPredictController (`/situation-predict`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/situation-predict` | 列出态势预测 | WebResult |
| POST | `/situation-predict` | 添加态势预测 | WebResult |
| PUT | `/situation-predict` | 更新态势预测 | WebResult |
| DELETE | `/situation-predict/{id}` | 删除态势预测 | WebResult |

---

## 14. 拓扑

### TopGraphController (`/top-graph`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/top-graph` | 获取拓扑图 | WebResult |

---

## 15. 导出

### ReportExportController (`/report-export`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/report-export` | 列出报表导出 | WebResult |
| POST | `/report-export` | 开始报表导出 | WebResult |

---

## 16. ITA 任务

### ItaTaskController (`/ita-task`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/ita-task` | 列出 ITA 任务 | WebResult |
| POST | `/ita-task` | 添加 ITA 任务 | WebResult |
| PUT | `/ita-task` | 更新 ITA 任务 | WebResult |
| DELETE | `/ita-task/{id}` | 删除 ITA 任务 | WebResult |

---

## 17. 指令

### InstructionController (`/instruction`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/instruction` | 列出指令 | WebResult |
| POST | `/instruction` | 添加指令 | WebResult |
| PUT | `/instruction` | 更新指令 | WebResult |
| DELETE | `/instruction/{id}` | 删除指令 | WebResult |

---

## 18. BI 任务

### BiTaskController (`/bi-task`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/bi-task` | 列出 BI 任务 | WebResult |
| POST | `/bi-task` | 添加 BI 任务 | WebResult |
| PUT | `/bi-task` | 更新 BI 任务 | WebResult |
| DELETE | `/bi-task/{id}` | 删除 BI 任务 | WebResult |

---

## 19. 监控

### ScreenMonitorController (`/screen-monitor`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/screen-monitor` | 获取屏幕监控数据 | WebResult |

---

## 20. 日志

### LogController (`/log`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| GET | `/log` | 列出日志 | WebResult |
| GET | `/log-detail` | 获取日志详情 | WebResult |

---

## 21. Open API

### OpenController (`/open`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| Various | `/open/*` | Open API 端点 | WebResult |

---

## 22. OEM 搜索

### OemSearchController (`/oem-search`)
| Method | Path | Description | Return Type |
|--------|------|-------------|-------------|
| POST | `/oem-search` | OEM 搜索 | WebResult |

---

## WebSocket 端点

### WebSocketChat (WebSocket 协议)
- Endpoint: `/ws/chat` (通过 `WebSocketConfig` 配置)
- Type: WebSocket 连接，用于实时聊天/消息推送

### WebSocketConfig
- 配置 WebSocket 处理程序映射
- 处理连接打开、关闭、错误和消息事件
