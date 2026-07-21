# EXPORTS.md — report-engine 出口契约

> 版本: 1.0.0
> 维护: report-engine 模块负责人

## 一、报表统计服务

### 定时任务

| 任务 | Cron 表达式 | 频率 | 数据源 | 输出表 |
|------|------------|------|--------|--------|
| `reportTasks` | `0 0/10 * * * ?` | 每 10 分钟 | risk_result + statistic_result | 8 种报表表 |
| `suspiciousListTask` | `0 0 0/1 * * ?` | 每小时 | risk_result | report_suspicious_list + suspicious_list |
| `reportDailyTasks` | `0 30 1 */5 * ?` | 每 5 月 1 号 1:30 | report_main_stats | report_model_user, report_model_src_ip, report_user_oper, report_sens_tool, Redis |
| `statisticsTasks` | `0 30 1 * * ?` | 每日 1:30 | 多表 | daily_statistics |

## 二、报表统计维度与输出表

### 报表类型与输出表

| 报表类型 | 输出表 | 聚合维度 | 统计字段 |
|---------|--------|---------|---------|
| 风险计数 | `report_risk_count` | 小时 + 风险等级 + 风险类型 | 高风险/中风险/低风险计数及占比 |
| 主统计 | `report_main_stats` | 小时 + 模型 + 风险类型 + 资产 + 工具 + 操作类型 + 访问敏感 | 风险数量 |
| 敏感风险 | `report_sens_risk` | 小时 + 资产 + 数据库 + 表 + 字段 | 访问次数 |
| 资产风险 | `report_asset_risk` | 资产 + 风险等级 | 高/中/低风险数 + 关注数 + 源IP + 模型风险 |
| 模型用户 | `report_model_user` | 日 + 模型 + 风险类型 + 资产 + 用户 | 风险数量 |
| 模型源IP | `report_model_src_ip` | 日 + 模型 + 风险类型 + 资产 + 源IP | 风险数量 |
| 用户操作 | `report_user_oper` | 日 + 操作类型 + 用户 + 风险等级 + 资产 | 风险数量 |
| 敏感工具 | `report_sens_tool` | 日 + 访问敏感 + 操作类型 + 工具 + 风险等级 + 风险类型 | 风险数量 |
| 可疑列表 | `report_suspicious_list` | 小时 + IP + MAC + 数据用户 + 数据库用户 | 高/中/低风险计数 |
| 每日资产统计 | `daily_statistics` | 日 + 资产 | 漏洞数(高/中/低/信息) + 攻击风险 + 访问风险 + 系统风险 + 弱口令数 |

### 可疑列表风险等级计算

使用 6 个时间窗口阈值矩阵（1h, 6h, 12h, 24h, 15d, 30d），根据高风险/中风险/低风险数量逐级判定：
- 初始值从矩阵获取
- 检查高风险 >= 初始值，满足则升级
- 否则检查中风险 >= 调整后的初始值
- 否则检查低风险 >= 调整后的初始值
- 最终等级：0=高, 1=中, 2=低

## 三、Redis 缓存

### Key 格式

| Key 模式 | 示例 | 用途 |
|---------|------|------|
| `modelDay#YYYY-MM-DD` | `modelDay#2024-01-01` | 按日期存储模型风险 |
| `modelDay#YYYY-MM-DD#IP#PORT` | `modelDay#2024-01-01#10.0.0.1#3306` | 按日期+资产存储模型风险 |

### Field 格式

`{modelName}>>riskNum` — 模型名称与风险数量

### 操作

- `hset(key, field, riskNum)` — 保存模型风险数据
- `keys("modelDay#*")` — 列出所有模型风险键

## 四、MySQL 报表表结构

| 表名 | 主键 | 核心字段 | 说明 |
|------|------|---------|------|
| `report_risk_count` | id | stats_time, high_risk, med_risk, low_risk, attack_risk, access_risk, model_event, sens_access_event | 小时风险计数 |
| `report_main_stats` | id | stats_time, model_name, risk_type, db_ip, db_port, use_tool, oper_type, access_sens, risk_num | 小时主统计 |
| `report_sens_risk` | id | stats_time, db_ip, db_port, db_name, table_name, field_name, access_num | 小时敏感风险 |
| `report_asset_risk` | id | db_ip, db_port, high_risk, med_risk, low_risk, attention_num, src_ip, model_risk | 资产风险汇总 |
| `report_model_user` | id | stats_time, model_name, risk_type, db_ip, db_port, user_name, risk_num | 每日模型用户统计 |
| `report_model_src_ip` | id | stats_time, model_name, risk_type, db_ip, db_port, src_ip, risk_num | 每日模型源IP统计 |
| `report_user_oper` | id | stats_time, oper_type, user_name, risk_lev, db_ip, db_port, risk_num | 每日用户操作统计 |
| `report_sens_tool` | id | stats_time, access_sens, oper_type, use_tool, risk_lev, risk_type, risk_num | 每日敏感工具统计 |
| `report_suspicious_list` | suspicious_list_id | suspicious_list_ip, high_number, medium_number, low_number | 每小时可疑列表预统计 |
| `suspicious_list` | suspicious_id | suspicious_ip, suspicious_level, create_time | 最终可疑列表 |
| `daily_statistics` | id | db_ip, db_port, time, high_bug, med_bug, low_bug, high_attack, med_attack, low_attack, wp_num | 每日资产统计 |

## 五、服务注册

- Zookeeper 路径: `/services/reportengine`
- 注册数据: `{host}:{port}`
- 节点类型: `EPHEMERAL`（进程退出自动删除）
