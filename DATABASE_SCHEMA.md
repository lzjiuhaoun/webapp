# DATABASE_SCHEMA.md — MySQL 数据库表结构

## 概述

系统使用 MySQL 作为主数据库（`aas_simp`），同时支持 DM 达梦数据库。以下列出所有模块使用的表结构。

---

## kafka-client 模块 (19 张表)

### 1. sys_log — 系统日志

| 字段 | 类型 | 说明 |
|------|------|------|
| system_log_id | INTEGER | 系统日志 ID (主键) |
| log_time | BIGINT | 系统日志时间 |
| log_type | BIT | 日志类型: 0通信/1异常/2其他/3设备/4版本更新 |
| log_status | BIT | 日志状态: 1成功/0失败 |
| log_desc | VARCHAR | 日志描述 |
| log_level | BIT | 日志级别: 2一般事件/1一般告警/0致命告警 |

### 2. sens_type — 敏感类型

| 字段 | 类型 | 说明 |
|------|------|------|
| sens_id | INTEGER | 敏感类型 ID (主键) |
| sens_attribute | INTEGER | 敏感属性 |
| sens_name | VARCHAR | 敏感类型名称 |
| sens_area_num | INTEGER | 敏感区域编号 |
| sens_title | VARCHAR | 标题 |
| sens_content | VARCHAR | 内容 |
| sens_cover | VARCHAR | 封面 |
| sens_random | VARCHAR | 随机值 |
| sens_replace | VARCHAR | 替换值 |
| sens_mapping | VARCHAR | 映射值 |
| sens_sha256 | VARCHAR | SHA256 值 |
| sens_separator | VARCHAR | 分隔符 |
| sens_length | INTEGER | 长度 |
| sens_example | VARCHAR | 示例 |

### 3. sens_type_detail — 敏感类型详情

| 字段 | 类型 | 说明 |
|------|------|------|
| sens_type_detail_id | INTEGER | 敏感类型详情 ID (主键) |
| sens_id | INTEGER | 敏感类型 ID (外键) |
| area_code | VARCHAR | 区域代码 |
| area_length | INTEGER | 区域长度 |
| end_mark | VARCHAR | 结束标记 |
| check_type | INTEGER | 检查类型 |
| data_dictionary_id | INTEGER | 数据字典 ID |
| other_sens_id | INTEGER | 其他敏感类型 ID |
| value_type | INTEGER | 值类型 |
| value_min | VARCHAR | 最小值 |
| value_max | VARCHAR | 最大值 |
| regular | VARCHAR | 正则表达式 |
| data_type | VARCHAR | 数据类型 |
| data_value | VARCHAR | 数据值 |
| segment_checked | INTEGER | 分段检查 |

### 4. safety_dev_config — 安全设备配置

| 字段 | 类型 | 说明 |
|------|------|------|
| dev_id | INTEGER | 能力单元 ID (主键) |
| dev_uuid | VARCHAR | 能力单元 UUID |
| dev_name | VARCHAR | 能力单元名称 |
| dev_type_id | TINYINT | 设备类型 ID |
| dev_enable | TINYINT | 是否启用 |
| dev_ip | VARCHAR | 能力单元 IP |
| dev_port | INTEGER | 端口 |
| manager_id | INTEGER | 负责人 ID |
| dev_dept | VARCHAR | 部门 |
| dev_desc | VARCHAR | 描述 |
| dev_statu | TINYINT | 状态: 0离线/1在线/2逻辑删除 |
| dev_account | VARCHAR | 连接账号 |
| dev_password | VARCHAR | 连接密码 |
| dev_auth_url | VARCHAR | 连接地址 |
| dev_token | VARCHAR | 认证 token |
| collection_data_type | VARCHAR | 采集类型 |
| collection_method | TINYINT | 采集方式: 0kafka/1syslog/2Restapi |
| collection_encoding | VARCHAR | 字符编码 |
| parser_rules | VARCHAR | 解析规则 ID 集合 |
| snmp_version | VARCHAR | SNMP 版本 |
| snmp_name | VARCHAR | SNMP 用户名/团体名 |
| sec_lev | VARCHAR | 安全级别 |
| auth_agreement | VARCHAR | 认证协议 |
| auth_password | VARCHAR | 认证密码 |
| ecp_agreement | VARCHAR | 报文加密协议 |
| ecp_password | VARCHAR | 报文加密密码 |
| collection_period | INTEGER | 采集周期 |
| extend_a | VARCHAR | 扩展字段 A |
| extend_b | VARCHAR | 扩展字段 B |
| extend_c | VARCHAR | 扩展字段 C |
| physical_id | INTEGER | 物理 ID |
| logic_area_id | INTEGER | 逻辑区域 ID |
| ipaddr | VARCHAR | IP 地址 |
| ipflag | INTEGER | IP 标志 |

### 5. find_task — 敏感数据发现任务

| 字段 | 类型 | 说明 |
|------|------|------|
| find_task_id | INTEGER | 任务 ID (主键) |
| db_uuid | VARCHAR | 数据库 UUID |
| execution_mode | INTEGER | 执行模式 |
| execution_time | BIGINT | 执行时间 |
| execution_cycle | VARCHAR | 执行周期 |
| task_start_time | BIGINT | 任务开始时间 |
| task_end_time | BIGINT | 任务结束时间 |
| task_total_time | BIGINT | 任务总时间 |
| task_status | INTEGER | 任务状态 |
| table_sum | INTEGER | 表总数 |
| table_deal | INTEGER | 已处理表数 |
| sens_mode_data | VARCHAR | 敏感模式数据 |
| sens_match_rate | VARCHAR | 敏感匹配率 |
| task_rate | INTEGER | 任务进度 |
| task_total | INTEGER | 任务总数 |
| db_status | BIT | 数据库状态 |

### 6. find_info — 发现任务信息

| 字段 | 类型 | 说明 |
|------|------|------|
| find_info_id | INTEGER | 任务信息 ID (主键) |
| find_task_id | INTEGER | 任务 ID |
| schema_name | VARCHAR | 模式名 |
| table_name | VARCHAR | 表名 |
| db_name | VARCHAR | 数据库名 |

### 7. find_details — 发现任务详情

| 字段 | 类型 | 说明 |
|------|------|------|
| find_details_id | INTEGER | 任务详情 ID (主键) |
| find_task_id | INTEGER | 任务 ID |
| database_name | VARCHAR | 数据库名 |
| table_name | VARCHAR | 表名 |
| field_name | VARCHAR | 字段名 |
| job_status | INTEGER | 作业状态 |
| thread_id | VARCHAR | 线程 ID |
| matching_rate | VARCHAR | 匹配率 |
| sens_id | VARCHAR | 敏感类型 ID |
| is_sensdata | VARCHAR | 是否敏感数据 |

### 8. dm_file_task — 文件脱敏任务

| 字段 | 类型 | 说明 |
|------|------|------|
| dm_file_id | INTEGER | 文件脱敏 ID (主键) |
| file_task_id | BIGINT | 文件任务 ID |
| file_task_name | VARCHAR | 任务名称 |
| target_type | INTEGER | 目标类型 |
| task_start_time | BIGINT | 任务开始时间 |
| task_end_time | BIGINT | 任务结束时间 |
| task_status | VARCHAR | 任务状态 |
| src_file_name | VARCHAR | 源文件名 |
| tar_file_name | VARCHAR | 目标文件名 |
| tar_db_config_ip | VARCHAR | 目标数据库 IP |
| tar_db_config_port | INTEGER | 目标数据库端口 |
| tar_db_config_sid | VARCHAR | 目标数据库 SID |
| tar_db_config_type | VARCHAR | 目标数据库类型 |
| tar_db_config_db_example | VARCHAR | 目标数据库名 |
| dev_uuid | VARCHAR | 能力单元 UUID |
| is_delete | INTEGER | 是否删除: 0未删/1已删 |

### 9. dm_desen_table_result — 脱敏表结果

| 字段 | 类型 | 说明 |
|------|------|------|
| table_result_id | INTEGER | 表结果 ID (主键) |
| dm_db_id | INTEGER | 数据库脱敏 ID (外键) |
| src_desen_table_name | VARCHAR | 源脱敏表名 |
| tar_desen_table_name | VARCHAR | 目标脱敏表名 |
| no_desen_rate | DOUBLE | 未脱敏占比 |

### 10. dm_desen_field_result — 脱敏字段结果

| 字段 | 类型 | 说明 |
|------|------|------|
| field_result_id | INTEGER | 字段结果 ID (主键) |
| table_result_id | INTEGER | 表结果 ID (外键) |
| field_name | VARCHAR | 字段名 |
| sens_name | VARCHAR | 敏感类型名 |

### 11. dm_db_task — 数据库脱敏任务

| 字段 | 类型 | 说明 |
|------|------|------|
| dm_db_id | INTEGER | 数据库脱敏 ID (主键) |
| db_task_id | BIGINT | 脱敏任务 ID |
| db_task_name | VARCHAR | 脱敏任务名称 |
| target_type | INTEGER | 脱敏任务类型 |
| task_start_time | BIGINT | 任务开始时间 |
| task_end_time | BIGINT | 任务结束时间 |
| task_status | VARCHAR | 脱敏任务状态 |
| src_db_config_ip | VARCHAR | 源数据 IP |
| src_db_config_port | INTEGER | 源数据端口 |
| src_db_config_sid | VARCHAR | 源数据 SID |
| src_db_config_type | VARCHAR | 源数据类型 |
| src_db_config_db_example | VARCHAR | 源数据库名 |
| tar_db_config_ip | VARCHAR | 目标数据库 IP |
| tar_db_config_port | INTEGER | 目标数据库端口 |
| tar_db_config_sid | VARCHAR | 目标数据库 SID |
| tar_db_config_type | VARCHAR | 目标数据库操作类型 |
| tar_db_config_db_example | VARCHAR | 目标数据库名 |
| tar_file_name | VARCHAR | 目标文件名 |
| dev_uuid | VARCHAR | 能力单元 UUID |
| is_delete | INTEGER | 是否删除: 0未删/1已删 |

### 12. dev_alarm_history — 能力单元告警历史

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 告警 ID (主键) |
| dev_uuid | VARCHAR | 能力单元 UUID |
| dev_type_id | INTEGER | 设备类型 |
| dev_ip | VARCHAR | 消息来源 IP |
| dev_port | INTEGER | 消息来源端口 |
| alarm_type | VARCHAR | 告警类型 |
| alarm_msg | VARCHAR | 告警消息内容 |
| happen_time | BIGINT | 发生时间 |
| report_time | BIGINT | 上报时间 |
| create_time | BIGINT | 数据新增时间 |

### 13. db_statistics_info — 数据库统计信息

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| db_conf_uuid | VARCHAR | 资产 UUID |
| db_name | VARCHAR | 数据库名 |
| db_table | VARCHAR | 表名 |
| db_fileds | VARCHAR | 字段名 |
| db_fileds_type | VARCHAR | 字段类型 |
| db_table_desc | VARCHAR | 表描述 |
| db_fileds_desc | VARCHAR | 字段描述 |
| db_fileds_index | TINYINT | 字段索引 |

### 14. db_judge — 数据库判断

| 字段 | 类型 | 说明 |
|------|------|------|
| db_judge_id | INTEGER | 判断 ID (主键) |
| dev_uuid | VARCHAR | 能力单元 UUID |
| db_name | VARCHAR | 数据库名 |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| db_type_str | VARCHAR | 数据库类型字符串 |
| judge_status | TINYINT | 判断状态 |
| judge_time | BIGINT | 判断时间 |

### 15. db_conf — 数据库资产配置

| 字段 | 类型 | 说明 |
|------|------|------|
| db_uuid | VARCHAR | 数据库 UUID (主键) |
| db_id | INTEGER | 数据库 ID |
| db_name | VARCHAR | 数据库名 |
| db_example | VARCHAR | 数据库示例 |
| db_ip | VARCHAR | 数据库 IP |
| db_port | VARCHAR | 数据库端口 |
| db_schema | VARCHAR | 数据库模式 |
| db_type_id | INTEGER | 数据库类型 ID |
| db_sid | VARCHAR | 数据库 SID |
| db_role | VARCHAR | 数据库角色 |
| depart_id | INTEGER | 部门 ID |
| db_account | VARCHAR | 数据库账号 |
| db_password | VARCHAR | 数据库密码 |
| db_version | VARCHAR | 数据库版本 |
| db_encoding | TINYINT | 数据库编码 |
| db_flag | VARCHAR | 数据库标志 |
| db_desc | VARCHAR | 数据库描述 |
| db_effective | TINYINT | 是否有效 |
| manager_id | INTEGER | 负责人 ID |

### 16. db_conf_extend — 数据库资产扩展

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| db_conf_uuid | VARCHAR | 资产 UUID |
| db_role | VARCHAR | 数据库角色 |
| db_sid | VARCHAR | 数据库 SID |
| db_schema | VARCHAR | 数据库模式 |
| db_password | VARCHAR | 数据库密码 |

### 17. db_asset_flow_session — 数据库资产流量会话

| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR | 会话 ID (主键) |
| create_time | BIGINT | 创建时间 |
| assets_name | VARCHAR | 资产名 |
| ip | VARCHAR | IP 地址 |
| port | INTEGER | 端口 |
| flow_value | DECIMAL | 流量值 |
| session_count | BIGINT | 会话数 |
| dev_uuid | VARCHAR | 能力单元 UUID |

### 18. database_types — 数据库类型

| 字段 | 类型 | 说明 |
|------|------|------|
| db_type_id | INTEGER | 数据库类型 ID (主键) |
| db_type_value | VARCHAR | 数据库类型值 |
| bus_type_id | INTEGER | 业务类型 ID |

### 19. database_info — 数据库信息

| 字段 | 类型 | 说明 |
|------|------|------|
| database_info_id | INTEGER | 数据库信息 ID (主键) |
| db_uuid | INTEGER | 数据库 UUID |
| schema_name | VARCHAR | 模式名 |
| table_name | VARCHAR | 表名 |
| table_comment | VARCHAR | 表注释 |
| field_name | VARCHAR | 字段名 |
| field_comment | VARCHAR | 字段注释 |
| field_type | VARCHAR | 字段类型 |
| field_length | VARCHAR | 字段长度 |
| db_name | VARCHAR | 数据库名 |

### 20. assets_classify_level — 资产分类分级

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| tactics_id | INTEGER | 标准 ID |
| db_uuid | VARCHAR | 资产 UUID |
| db_name | VARCHAR | 资产名 |
| host | VARCHAR | 资产 IP |
| port | INTEGER | 资产端口 |
| dbType_name | VARCHAR | 资产类型名称 |
| database_name | VARCHAR | 数据库实例名 |
| table_name | VARCHAR | 表名 |
| table_describe | VARCHAR | 表描述 |
| table_category_name | VARCHAR | 表分类 |
| table_level_name | VARCHAR | 表分级 |
| field_name | VARCHAR | 字段名 |
| field_describe | VARCHAR | 字段描述 |
| field_type | VARCHAR | 字段类型 |
| field_length | VARCHAR | 字段长度 |
| field_class | VARCHAR | 字段类别 |
| field_category_name | VARCHAR | 字段分类 |
| field_level_name | VARCHAR | 字段分级 |
| field_level_value | INTEGER | 字段分级值 |
| matching_rate | VARCHAR | 匹配率 |
| matching_result | VARCHAR | 匹配结果 |
| sens_id | VARCHAR | 敏感类型 ID |
| schema | VARCHAR | 模式名 |
| business_system | VARCHAR | 业务系统 |
| organization | VARCHAR | 组织名 |
| scan_time | VARCHAR | 扫描时间 |
| md5 | VARCHAR | MD5 值 |

---

## web-app 模块 — 席位告警配置 (新增)

### 22. alert_access_list — 告警黑白名单

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键 (自增) |
| name | VARCHAR(64) | 规则名称 |
| type | TINYINT | 类别: 0=白名单, 1=黑名单 |
| status | TINYINT | 状态: 0=禁用, 1=启用 |
| remark | VARCHAR(255) | 备注 |
| create_time | BIGINT | 创建时间 |
| update_time | BIGINT | 更新时间 |
| is_delete | TINYINT | 软删除: 0=未删, 1=已删 |

### 23. alert_access_list_user — 黑白名单用户明细

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键 (自增) |
| list_id | INT | 黑白名单 ID (外键, 关联 alert_access_list.id) |
| user_name | VARCHAR(64) | 用户名 |
| user_account | VARCHAR(64) | 账号 |

索引: `idx_list_id` on `list_id`

---

### 24. alert_rule — 告警规则

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键 (自增) |
| name | VARCHAR(64) | 规则名称 |
| description | VARCHAR(255) | 描述 |
| log_type | TINYINT | 日志类型: 0=平台登录日志, 1=IM登录日志, 2=DLP日志 |
| rule_type | TINYINT | 规则类型: 0=普通规则, 1=组合规则 |
| level | TINYINT | 告警等级: 1=一级红, 2=二级橙, 3=三级黄 |
| link_type | TINYINT | 关联方式: 0=与, 1=或 |
| conditions | TEXT | 匹配条件 JSON |
| combine_duration | INT | 组合规则统计频率(分钟) |
| combine_count | INT | 组合规则触发次数阈值 |
| combine_rule_id | INT | 组合规则引用的普通规则 ID |
| whitelist_id | INT | 白名单 ID (FK → alert_access_list.id) |
| blacklist_id | INT | 黑名单 ID (FK → alert_access_list.id) |
| status | TINYINT | 状态: 0=禁用, 1=启用 |
| create_time | BIGINT | 创建时间 |
| update_time | BIGINT | 更新时间 |
| is_delete | TINYINT | 软删除: 0=未删, 1=已删 |

### 25. alert_result — 告警事件

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键 (自增) |
| alert_rule_id | INT | 命中规则 ID |
| rule_type | TINYINT | 规则类型: 0=普通, 1=组合 |
| normal_rule_id | INT | 组合规则引用的普通规则 ID |
| alert_level | TINYINT | 告警等级: 1=红, 2=橙, 3=黄 |
| trigger_count | INT | 实际触发次数 (默认 1) |
| create_time | BIGINT | 创建时间 |

索引: `idx_alert_rule_id` on `alert_rule_id`，`idx_create_time` on `create_time`

### 26. alert_result_audit — 告警事件审计日志明细

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键 (自增) |
| alert_result_id | INT | 告警事件 ID (FK → alert_result.id) |
| audit_log_id | VARCHAR(36) | 审计源数据全局 ID (UUID) |
| ip_address | VARCHAR(45) | 席位 IP |

索引: `idx_alert_result_id` on `alert_result_id`，`idx_ip_address` on `ip_address`

---

### 21. change_log — 变更日志

| 字段 | 类型 | 说明 |
|------|------|------|
| changelog_id | INTEGER | 日志 ID (主键) |
| dev_name | VARCHAR | 能力单元名称 |
| db_name | VARCHAR | 数据库名 |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| db_type_str | VARCHAR | 数据库类型字符串 |
| oper_type | VARCHAR | 操作类型 |
| oper_time | BIGINT | 操作时间 |

---

## manager-center 模块 (1 张表)

### message_routing — 消息路由表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| message_type | TINYINT | 消息类型 |
| content_type | TINYINT | 内容类型 |
| module_serversocket_host | VARCHAR | 模块 Socket 主机 |
| module_serversocket_port | INTEGER | 模块 Socket 端口 |
| extend_a | VARCHAR | 扩展字段 A |
| extend_b | VARCHAR | 扩展字段 B |
| extend_c | VARCHAR | 扩展字段 C |

---

## report-engine 模块 (10 张表)

### 1. report_user_oper — 用户操作报表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| stats_time | BIGINT | 统计时间 (主键) |
| oper_type | VARCHAR | 操作类型 |
| user_name | VARCHAR | 用户名 |
| risk_lev | TINYINT | 风险等级 |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| risk_num | INTEGER | 风险数量 |

### 2. report_sens_tool — 敏感工具报表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| stats_time | BIGINT | 统计时间 (主键) |
| access_sens | TINYINT | 是否访问敏感数据 |
| oper_type | VARCHAR | 操作类型 |
| use_tool | VARCHAR | 使用工具 |
| risk_lev | TINYINT | 风险等级 |
| risk_type | TINYINT | 风险类型 |
| risk_num | INTEGER | 风险数量 |

### 3. report_sens_risk — 敏感风险报表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| stats_time | BIGINT | 统计时间 |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| db_name | VARCHAR | 数据库名 |
| table_name | VARCHAR | 表名 |
| field_name | VARCHAR | 字段名 |
| access_num | INTEGER | 访问次数 |

### 4. report_risk_count — 风险计数报表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| stats_time | BIGINT | 统计时间 |
| high_risk | INTEGER | 高风险数 |
| high_risk_proc | INTEGER | 高风险比例 |
| med_risk | INTEGER | 中风险数 |
| med_risk_proc | INTEGER | 中风险比例 |
| low_risk | INTEGER | 低风险数 |
| low_risk_proc | INTEGER | 低风险比例 |
| attack_risk | INTEGER | 攻击风险数 |
| attack_risk_proc | INTEGER | 攻击风险比例 |
| access_risk | INTEGER | 访问风险数 |
| access_risk_proc | INTEGER | 访问风险比例 |
| model_event | INTEGER | 模型事件数 |
| model_event_proc | INTEGER | 模型事件比例 |
| model_risk | INTEGER | 模型风险数 |
| model_risk_proc | INTEGER | 模型风险比例 |
| model_hrisk | INTEGER | 模型高风险数 |
| model_hrisk_proc | INTEGER | 模型高风险比例 |
| sens_access_event | INTEGER | 敏感访问事件数 |
| sens_event_proc | INTEGER | 敏感事件比例 |
| sens_access_risk | INTEGER | 敏感访问风险数 |
| sens_risk_proc | INTEGER | 敏感风险比例 |
| insert_marker | INTEGER | 插入标记 |

### 5. report_model_user — 模型用户报表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| stats_time | BIGINT | 统计时间 (主键) |
| model_name | VARCHAR | 模型名称 |
| risk_type | TINYINT | 风险类型 |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| user_name | VARCHAR | 用户名 |
| risk_num | INTEGER | 风险数量 |

### 6. report_model_src_ip — 模型源 IP 报表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| stats_time | BIGINT | 统计时间 (主键) |
| model_name | VARCHAR | 模型名称 |
| risk_type | TINYINT | 风险类型 |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| src_ip | VARCHAR | 源 IP |
| risk_num | INTEGER | 风险数量 |

### 7. report_main_stats — 主统计报表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| stats_time | BIGINT | 统计时间 (主键) |
| model_name | VARCHAR | 模型名称 |
| risk_type | TINYINT | 风险类型 |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| use_tool | VARCHAR | 使用工具 |
| oper_type | VARCHAR | 操作类型 |
| access_sens | TINYINT | 是否访问敏感数据 |
| risk_num | INTEGER | 风险数量 |
| src_ip | LONGVARCHAR | 源 IP |
| user_name | LONGVARCHAR | 用户名 |
| risk_lev | TINYINT | 风险等级 |

### 8. report_asset_risk — 资产风险报表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| high_risk | INTEGER | 高风险数 |
| med_risk | INTEGER | 中风险数 |
| low_risk | INTEGER | 低风险数 |
| attention_num | INTEGER | 关注数 |
| src_ip | LONGVARCHAR | 源 IP |
| model_risk | LONGVARCHAR | 模型风险 |

### 9. daily_statistics — 每日资产统计

| 字段 | 类型 | 说明 |
|------|------|------|
| db_uuid | VARCHAR | 数据库 UUID |
| db_ip | VARCHAR | 数据库 IP |
| db_port | INTEGER | 数据库端口 |
| time | BIGINT | 统计时间 |
| access_times | INTEGER | 访问次数 |
| high_bug | INTEGER | 高危漏洞数 |
| med_bug | INTEGER | 中危漏洞数 |
| low_bug | INTEGER | 低危漏洞数 |
| info_bug | INTEGER | 信息漏洞数 |
| high_attack | INTEGER | 高危攻击数 |
| med_attack | INTEGER | 中危攻击数 |
| low_attack | INTEGER | 低危攻击数 |
| high_access | INTEGER | 高危访问数 |
| med_access | INTEGER | 中危访问数 |
| low_access | INTEGER | 低危访问数 |
| high_system_poex | INTEGER | 高危系统漏洞 |
| med_system_poex | INTEGER | 中危系统漏洞 |
| low_system_poex | INTEGER | 低危系统漏洞 |
| bug_total | INTEGER | 漏洞总数 |
| wp_num | INTEGER | 弱密码数 |

---

## collection-engine 模块 (2 张表)

### topic_relation_info — Topic 关系信息

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | ID (主键) |
| kafka_topic | VARCHAR | Kafka 主题名 |
| log_type | TINYINT | 日志类型 |
| log_name | VARCHAR | 日志类型名称 |
| db_type | TINYINT | 日志存储数据库类型 |
| db_name | VARCHAR | 日志存储数据库名 |
| table_name | VARCHAR | 表名 |
| log_propertites | VARCHAR | 日志对象属性 |
| table_fields | VARCHAR | 字段 |
| dev_type | TINYINT | 能力单元类型 |
| is_retrieval | TINYINT | 是否检索 |
| extend_a | VARCHAR | 扩展字段 A |
| extend_b | VARCHAR | 扩展字段 B |
| extend_c | VARCHAR | 扩展字段 C |

### safety_dev_config — 安全设备配置 (与 kafka-client 同名)
