# TASKS.md — report-engine 局部任务书

## 阶段 1: 报表预统计

### 任务 1.1: 报表任务框架
- [ ] `ReportStats` 类实现定时调度
- [ ] `@EnableScheduling` 注解
- [ ] `reportTasks()` 每 10 分钟触发
- [ ] 获取 ID 范围（risk_result + statistic_result）
- [ ] 查询 point_record 偏移量

### 任务 1.2: 数据聚合（ReportPretreatment）
- [ ] `getRiskCount()` — 风险计数聚合
- [ ] `getMainStats()` — 主统计聚合
- [ ] `getSensRisk()` — 敏感风险聚合
- [ ] `getAssetRisk()` — 资产风险聚合
- [ ] `DataCountModel` 数据模型

### 任务 1.3: 报表写入
- [ ] `updateReportTable()` upsert 逻辑
- [ ] `updateSumByPrimaryKey()` 累加数值字段
- [ ] `insert()` 插入新记录
- [ ] report_asset_risk JSON 合并

### 任务 1.4: 风险计数构建器
- [ ] `RiskCountData` 类
- [ ] setRiskLev() — 按风险等级分组
- [ ] setRiskType() — 按风险类型分组
- [ ] setModelEvent() — 模型事件分组
- [ ] setModelRisk() — 模型风险分组
- [ ] setSensEvent() — 敏感事件分组
- [ ] setSensRisk() — 敏感风险分组

## 阶段 2: 可疑列表预统计

### 任务 2.1: 可疑列表任务
- [ ] `suspiciousListTask()` 每小时触发
- [ ] 查询 risk_result 最近 1 小时数据
- [ ] 按 IP + MAC + 数据用户 + 数据库用户分组
- [ ] 统计高/中/低风险数量

### 任务 2.2: 可疑风险等级计算
- [ ] `suspiciousRiskLevelCalculation()` 计算逻辑
- [ ] 6 时间窗口矩阵（1h, 6h, 12h, 24h, 15d, 30d）
- [ ] 等级判定：0=高, 1=中, 2=低
- [ ] `suspiciousRiskLevelStorage()` 存储

### 任务 2.3: 可疑列表写入
- [ ] 插入 `report_suspicious_list`
- [ ] 合并到 `suspicious_list` 表
- [ ] 去重处理

## 阶段 3: 每日模型统计

### 任务 3.1: 每日模型统计任务
- [ ] `ReportStatsDaily` 类
- [ ] `@EnableScheduling` 注解
- [ ] `reportDailyTasks()` 每 5 月 1 号 1:30 触发
- [ ] 查询 report_main_stats 表

### 任务 3.2: 每日模型统计写入
- [ ] 按模型名称、风险类型、用户、源 IP 聚合
- [ ] 插入 report_model_user
- [ ] 插入 report_model_src_ip
- [ ] 插入 report_user_oper
- [ ] 插入 report_sens_tool

### 任务 3.3: Redis 模型风险存储
- [ ] `RedisConnect` 类
- [ ] `modelRiskMaxTime()` 获取最大时间
- [ ] `saveDataToRedis()` 保存模型风险
- [ ] `saveDataToRedisByDbIp()` 按资产 IP 保存
- [ ] Key 格式：modelDay#YYYY-MM-DD

### 任务 3.4: 数据清理
- [ ] 删除过期 report_main_stats 记录

## 阶段 4: 每日资产统计

### 任务 4.1: 每日资产统计任务
- [ ] `DailyStatistics` 类
- [ ] `@EnableScheduling` 注解
- [ ] `statisticsTasks()` 每日 1:30 触发
- [ ] 查询 db_conf 资产配置

### 任务 4.2: 资产统计计算
- [ ] `setBugNum()` 漏洞数统计
- [ ] `setWpNum()` 弱口令数统计
- [ ] `setAttackRisk()` 攻击风险统计
- [ ] `setAccessRisk()` 访问风险统计
- [ ] `setSystemPoexRisk()` 系统风险统计
- [ ] `setTotalBug()` 漏洞总数统计

### 任务 4.3: 每日资产统计写入
- [ ] 插入 daily_statistics 表
- [ ] 按资产、风险等级、漏洞等级分组

## 阶段 5: ES 审计日志查询

### 任务 5.1: SearchAuditLog
- [ ] `SearchAuditLog` 类
- [ ] `statisticsAccess()` 方法
- [ ] ES 连接配置（simp:9200）
- [ ] range 查询 happenTime
- [ ] terms 聚合 destIp + destPort
- [ ] ES 字段映射（AuditRecord.java）

## 阶段 6: 工具类

### 任务 6.1: ReportUtils
- [ ] `getLastTimeId()` 查询最大 ID
- [ ] `setAssetEvent()` 设置资产事件
- [ ] `setAssetSrcIp()` 设置资产源 IP
- [ ] `setAssetModelRisk()` 设置资产模型风险
- [ ] `mergeJsonStr()` 合并 JSON 字符串
- [ ] `mergeListStr()` 合并列表字符串
- [ ] `updateIdPoint()` 更新偏移量
- [ ] `staticSuspiciousList()` 可疑列表处理
- [ ] `getTogetherAndSuspiciousList()` 合并可疑列表

### 任务 6.2: CommonUtils
- [ ] `getDayBeforeTime()` 获取前一天时间

## 阶段 7: 服务集成

- [ ] `ReportEngineApplication` 启动
- [ ] Zookeeper 服务注册
- [ ] ProGuard 混淆打包
- [ ] AppAssembler + tar.gz 打包
