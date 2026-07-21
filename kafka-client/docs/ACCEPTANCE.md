# ACCEPTANCE.md — kafka-client 模块验收

## 验收标准

### T1: Kafka 消费
- [ ] 可成功连接到 Kafka 集群（SASL_PLAINTEXT + PLAIN 认证）
- [ ] 6 个消费者组均可正常创建并消费
- [ ] 各消费者组消费对应 Topic 正确
- [ ] maxPollRecords=500 批量消费正常
- [ ] 手动提交偏移量工作正常，重启后不重复消费
- [ ] enableAutoCommit=false 生效

### T2: 策略解析
- [ ] JsonDataAnalysis 可正确解析 JSON 对象 `{...}`
- [ ] JsonDataAnalysis 可正确解析 JSON 数组 `[...]`
- [ ] ListDataAnalysis 可正确解析 JSON 列表 `[{...}]`
- [ ] ApiV1JsonAnalysis 可正确解析 API v1.0 格式
- [ ] JsonDataPAnalysis 可正确解析 aas-p-log 数据
- [ ] 策略自动检测（开头字符判断）正确
- [ ] 防火墙格式数据可正确解析为 AuditRecordVo

### T3: ES 写入
- [ ] 审计日志可写入 aassimp-auditlog 索引
- [ ] API 审计日志可写入 aassimp-apiauditlog 索引
- [ ] aas-p-log 数据可写入 aassimp-auditlog 索引
- [ ] 字段映射正确，关键字段完整（id, happenTime, srcIp, destIp, 等）
- [ ] BulkRequest 批量写入机制工作正常
- [ ] 许可证容量限制生效（dataLogInfo * 100M 文档数）
- [ ] 容量超限时等待重试机制正常

### T4: MySQL 写入
- [ ] db_conf 表可正确写入/更新/删除
- [ ] db_judge 表可正确写入
- [ ] db_asset_flow_session 可批量写入
- [ ] sens_type/sens_type_detail 可正确写入
- [ ] dev_alarm_history 可正确写入
- [ ] classified/database_info 等分类分级表可正确写入
- [ ] dm_db_task/dm_desen_* 脱敏结果表可正确写入
- [ ] dm_file_task 可正确写入
- [ ] change_log 变更日志可正确记录

### T5: 线程池
- [ ] 6 个线程池按配置启动（core/max/keepAlive/queue）
- [ ] CallerRunsPolicy 拒绝策略生效
- [ ] 各线程池独立运行，互不阻塞
- [ ] 线程名格式正确

### T6: 许可证管控
- [ ] 启动时调用 checklic 脚本验证许可证
- [ ] LicenseInfo 正确解析（expiryTime, dataLogInfo 等）
- [ ] 过期消息被正确拦截（跳过不处理）
- [ ] ZK /data/licenseUpdateTime Watch 生效
- [ ] 许可证更新后及时刷新缓存

### T7: 敏感数据匹配
- [ ] Redis sens-type 缓存正确加载
- [ ] 审计日志可匹配敏感类型
- [ ] sensData=1 标记正确设置
- [ ] RedisService.hmset() 正确更新缓存

### T8: 定时清理
- [ ] SaticScheduleTask 每日凌晨 2:00 触发
- [ ] 可正确清理 cleanTopics 中已消费记录
- [ ] 清理结果记录到 sys_log 表

### T9: 异常处理
- [ ] 解析异常数据记录到 jsonExceptionDataDir
- [ ] 异常信息记录到 sys_log 表
- [ ] 异常处理不阻塞正常消息处理

### T10: 服务注册
- [ ] 启动后成功注册到 Zookeeper /services/kafkaclient
- [ ] Netty ES 冲突修复生效（es.set.netty.runtime.available.processors=false）
