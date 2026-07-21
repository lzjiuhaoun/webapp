# INTERNAL_LOGIC.md — report-engine 内部实现逻辑

> 本模块描述 report-engine 的内部实现细节。

## 一、启动流程

### 1. Spring Context 加载
- `@SpringBootApplication` 触发组件扫描
- `@MapperScan("com.ankki.reportengine.mapper")` 注册 MyBatis Mapper
- `@EnableScheduling` 启用定时调度（在 ReportStats, ReportStatsDaily, DailyStatistics 上）

### 2. 主线程启动
```java
public static void main(String[] args) {
    SpringApplication.run(ReportEngineApplication.class, args);
    // Zookeeper 连接
    ZookeeperClient zkcli = new ZookeeperClient("reportengine", "127.0.0.1:10000");
    zkcli.connectZk();
    zkcli.registerServerStandalone();
}
```

## 二、定时任务调度

### 任务 1: reportTasks — 报表预统计（每 10 分钟）

**流程** (`ReportStats.reportTasks()`):

1. **获取 ID 范围**
   - 查询 `risk_result` 表最大 ID
   - 查询 `statistic_result` 表最大 ID
   - 查询 `point_record` 表获取上次处理偏移量
   - 计算本次处理范围（risk_result 每批 200,000，statistic_result 每批 400,000）

2. **数据聚合** (`ReportPretreatment`)
   - `getRiskCount()` — 按小时、风险等级、风险类型、模型事件、敏感访问聚合
   - `getMainStats()` — 按小时、模型、风险类型、资产、工具、操作类型、访问敏感聚合
   - `getSensRisk()` — 按小时、资产、数据库、表、字段聚合敏感访问
   - `getAssetRisk()` — 按资产、风险等级聚合资产风险

3. **写入 MySQL** (`updateReportTable()`)
   - 对每个报表表，检查主键是否存在
   - 存在则 `updateSumByPrimaryKey()` 累加数值字段
   - 不存在则 `insert()` 插入新记录
   - report_asset_risk 特殊处理：合并 src_ip 和 model_risk JSON 字符串

4. **更新偏移量**
   - 更新 `point_record` 表，记录最大处理 ID

### 任务 2: suspiciousListTask — 可疑列表预统计（每小时）

**流程** (`ReportStats.suspiciousListTask()`):

1. 查询 `risk_result` 表最近 1 小时数据
2. 按 IP + MAC + 数据用户 + 数据库用户分组
3. 统计高/中/低风险数量
4. 计算可疑风险等级（`suspiciousRiskLevelCalculation()`）
5. 插入/更新 `report_suspicious_list` 表
6. 合并到 `suspicious_list` 表（去重，保留最新）

### 任务 3: reportDailyTasks — 每日模型统计（每 5 月 1 号 1:30）

**流程** (`ReportStatsDaily.reportDailyTasks()`):

1. 查询 `report_main_stats` 表数据（按日期聚合）
2. 按模型名称、风险类型、用户、源 IP 聚合
3. 插入 `report_model_user`、`report_model_src_ip`、`report_user_oper`、`report_sens_tool`
4. 保存模型风险到 Redis（`modelDay#日期` 格式）
5. 清理过期 `report_main_stats` 记录

### 任务 4: statisticsTasks — 每日资产统计（每日 1:30）

**流程** (`DailyStatistics.statisticsTasks()`):

1. 查询资产配置表 `db_conf`
2. 统计漏洞：查询 `vs_va_result` 按等级统计
3. 统计弱口令：查询 `vs_wp_result`
4. 统计攻击风险：查询 `risk_result` 按风险等级统计
5. 统计访问风险：查询 `statistic_result` 按风险等级统计
6. 统计系统风险：查询 `system_poex_risk`
7. 汇总漏洞总数
8. 插入 `daily_statistics` 表

## 三、风险计数计算逻辑

### RiskCountData 构建器

`RiskCountData` 类负责从 `DataCountModel` 列表构建 `ReportRiskCount` 对象：

- **setRiskLev()**: 按小时分组，按风险等级（0=高, 1=中, 2=低）求和
- **setRiskType()**: 按小时分组，name="0" 为攻击风险，其他为访问风险
- **setModelEvent()**: 按小时分组，所有事件计数
- **setModelRisk()**: 按小时分组，风险等级 <= 2 的模型风险
- **setModelHighRisk()**: 按小时分组，风险等级=0 的高风险
- **setSensEvent()**: 按小时分组，所有敏感访问事件
- **setSensRisk()**: 按小时分组，风险等级 <= 2 且风险类型=1 的敏感风险

### 可疑风险等级计算

使用 6 个时间窗口矩阵（1h, 6h, 12h, 24h, 15d, 30d）：
- 初始值从矩阵获取
- 检查高风险 >= 初始值，满足则升级
- 否则检查中风险 >= 调整后的初始值
- 否则检查低风险 >= 调整后的初始值
- 最终等级：0=高, 1=中, 2=低

## 四、报表聚合实现

### ReportPretreatment 核心方法

**getRiskCount()** — 风险计数聚合
```sql
SELECT time, risk_lev, risk_type, model_name, access_sens
FROM risk_result WHERE id BETWEEN ? AND ?
UNION ALL
SELECT end_time, risk_lev, risk_type, model_name, access_sens
FROM statistic_result WHERE id BETWEEN ? AND ?
```
按时间、风险等级、风险类型、模型、敏感访问聚合。

**getMainStats()** — 主统计聚合
```sql
SELECT time, model_name, risk_type, db_ip, db_port, use_tool, oper_type, access_sens, src_ip, user_name
FROM risk_result WHERE id BETWEEN ? AND ?
UNION ALL
SELECT end_time, model_name, risk_type, db_ip, db_port, use_tool, oper_type, access_sens, src_ip, user_name
FROM statistic_result WHERE id BETWEEN ? AND ?
```
按小时、模型、风险类型、资产、工具、操作类型、访问敏感、源 IP、用户聚合。

**getSensRisk()** — 敏感风险聚合
从 risk_result 表按 db_ip, db_port, db_name, table_name, field_name 聚合。

**getAssetRisk()** — 资产风险聚合
从 risk_result 和 statistic_result 表按 db_ip, db_port 聚合，计算高/中/低/关注风险数，收集源 IP 和模型风险 JSON。

### updateReportTable() — 报表写入

**upsert 逻辑**：
1. 查询主键是否存在
2. 存在 → `updateSumByPrimaryKey()` 累加数值字段
3. 不存在 → `insert()` 插入新记录
4. report_asset_risk 特殊处理：合并 src_ip 和 model_risk JSON

## 五、Redis 模型风险存储

### RedisConnect 实现

- **modelRiskMaxTime()**: 获取模型风险最大时间
- **saveDataToRedis(key, field, riskNum)**: 保存模型风险到 Redis
- **saveDataToRedisByDbIp(key, field, riskNum)**: 按资产 IP 保存

**Key 构造**：
- 日期格式: `modelDay#YYYY-MM-DD`
- 日期+资产格式: `modelDay#YYYY-MM-DD#IP#PORT`
- Field 格式: `{modelName}>>riskNum`

## 六、ES 审计日志查询

### SearchAuditLog 实现

**ES 连接配置**：
- Host: `simp:9200`（硬编码）
- 认证: `elastic:Ankki_ES123`
- 最大连接: 5
- 保活: 5 分钟

**查询方法**：`statisticsAccess(SelectDataModel timRange)`

**ES 查询结构**：
```
GET aassimp-auditlog/_search
{
  "query": {
    "range": {
      "happenTime": {
        "gte": startTime,
        "lt": endTime
      }
    }
  },
  "aggs": {
    "dbIp": {
      "terms": { "field": "destIp", "size": 128 },
      "aggs": {
        "dbPort": {
          "terms": { "field": "destPort" }
        }
      }
    }
  }
}
```

**ES 字段映射**（AuditRecord.java）：
- `happenTime` (Long) — 时间范围查询
- `destIp` (Keyword) — 聚合字段
- `destPort` (Integer) — 聚合字段
- `dbUser` (Keyword) — 数据库用户
- `operType` (Keyword) — 操作类型
- `riskLev` (Integer) — 风险等级
- `model_name` (Keyword) — 模型名称
- `access_sens` (Keyword) — 访问敏感标记

## 七、工具类实现

### ReportUtils 核心方法

- **getLastTimeId()**: 查询 risk_result 和 statistic_result 表最大 ID
- **setAssetEvent()**: 设置资产事件数据
- **setAssetSrcIp()**: 设置资产源 IP 数据
- **setAssetModelRisk()**: 设置资产模型风险数据
- **mergeJsonStr()**: 合并 JSON 字符串（用于 src_ip 和 model_risk）
- **mergeListStr()**: 合并列表字符串
- **updateIdPoint()**: 更新 point_record 偏移量
- **staticSuspiciousList()**: 静态可疑列表处理
- **getTogetherAndSuspiciousList()**: 合并可疑列表
- **suspiciousRiskLevelStorage()**: 可疑风险等级存储
- **suspiciousRiskLevelCalculation()**: 可疑风险等级计算
- **riskDataSum()**: 风险数据求和
- **quoteAttributeValue()**: 引用属性值

### CommonUtils

- **getDayBeforeTime()**: 获取前一天时间

## 八、异常处理

- 定时任务异常通过 Spring TaskScheduler 默认处理
- 数据库操作异常由 MyBatis 抛出
- Redis 操作异常由 RedisUtil 处理
- 日志记录使用 Spring 日志框架
