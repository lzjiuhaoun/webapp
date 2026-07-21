# ACCEPTANCE.md — report-engine 模块验收

## 验收标准

### T1: 报表预统计
- [ ] 每 10 分钟触发 reportTasks 定时任务
- [ ] 正确查询 risk_result + statistic_result 表
- [ ] 正确查询 point_record 表获取偏移量
- [ ] 数据聚合正确（风险计数、主统计、敏感风险、资产风险）
- [ ] 报表数据正确写入 8 种报表表
- [ ] upsert 逻辑正确（存在则累加，不存在则插入）
- [ ] point_record 偏移量正确更新

### T2: 可疑列表预统计
- [ ] 每小时触发 suspiciousListTask 定时任务
- [ ] 正确查询 risk_result 表最近 1 小时数据
- [ ] 按 IP + MAC + 数据用户 + 数据库用户分组
- [ ] 高/中/低风险数量统计正确
- [ ] 可疑风险等级计算正确（6 时间窗口矩阵）
- [ ] report_suspicious_list 表写入正确
- [ ] suspicious_list 表合并去重正确

### T3: 每日模型统计
- [ ] 每 5 月 1 号 1:30 触发 reportDailyTasks 定时任务
- [ ] 正确查询 report_main_stats 表
- [ ] 按模型名称、风险类型、用户、源 IP 聚合
- [ ] report_model_user、report_model_src_ip、report_user_oper、report_sens_tool 写入正确
- [ ] Redis 模型风险存储正确（modelDay#日期 格式）
- [ ] 过期 report_main_stats 记录正确清理

### T4: 每日资产统计
- [ ] 每日 1:30 触发 statisticsTasks 定时任务
- [ ] 正确查询 db_conf、vs_va_result、vs_wp_result 等表
- [ ] 漏洞数（高/中/低/信息）统计正确
- [ ] 弱口令数统计正确
- [ ] 攻击风险（高/中/低）统计正确
- [ ] 访问风险（高/中/低）统计正确
- [ ] 系统风险（高/中/低）统计正确
- [ ] daily_statistics 表写入正确

### T5: ES 审计日志查询
- [ ] SearchAuditLog 正确创建 ES 客户端
- [ ] ES 连接配置正确（simp:9200, elastic:Ankki_ES123）
- [ ] range 查询 happenTime 正确
- [ ] terms 聚合 destIp + destPort 正确
- [ ] ES 字段映射正确（AuditRecord.java）

### T6: Redis 模型风险存储
- [ ] RedisConnect 正确连接 Redis
- [ ] modelRiskMaxTime() 正确获取最大时间
- [ ] saveDataToRedis() 正确保存模型风险
- [ ] saveDataToRedisByDbIp() 正确按资产 IP 保存
- [ ] Key 格式正确（modelDay#YYYY-MM-DD）
- [ ] Field 格式正确（modelName>>riskNum）

### T7: 报表写入逻辑
- [ ] updateReportTable() upsert 逻辑正确
- [ ] updateSumByPrimaryKey() 累加数值字段正确
- [ ] insert() 插入新记录正确
- [ ] report_asset_risk JSON 合并正确
- [ ] src_ip 和 model_risk JSON 字符串合并正确

### T8: 服务注册
- [ ] 启动后成功注册到 Zookeeper /services/reportengine
- [ ] 注册数据正确
- [ ] 节点类型为 EPHEMERAL
