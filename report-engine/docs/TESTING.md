# TESTING.md — report-engine 测试策略

## 测试工具链

- JUnit 4 + Spring Boot Test
- Mockito — Mock MyBatis Mapper、Redis、Zookeeper、ES 客户端
- Embedded MySQL — 替代真实数据库进行 Mapper 测试
- Embedded Redis — 测试 Redis 操作
- H2 Database — 替代 MySQL 进行单元测试

## 测试目录结构

```
src/test/java/com/ankki/reportengine/
├── service/report/       # ReportStats, ReportStatsDaily, ReportPretreatment 测试
├── service/prestatistics/ # DailyStatistics, SearchAuditLog 测试
├── mapper/               # MyBatis Mapper 测试
├── model/                # 模型类测试
├── config/               # ElasticsearchConfig 测试
└── utils/                # ReportUtils, CommonUtils 测试
```

## Mock 边界

| 外部依赖 | Mock 方式 | 说明 |
|---------|----------|------|
| MyBatis Mapper | Mockito Mock | 返回预定义的报表数据 |
| Redis | Embedded Redis 或 Mock | 测试模型风险缓存 |
| Zookeeper | Curator Test Server 或 Mock | 测试服务注册 |
| Elasticsearch | Mock RestHighLevelClient | 测试审计日志查询 |
| MySQL | H2 内存数据库 | 测试报表写入 |

## 测试重点

1. **定时任务触发**：4 个定时任务按 Cron 表达式正确触发
2. **报表聚合逻辑**：getRiskCount/getMainStats/getSensRisk/getAssetRisk 聚合正确性
3. **可疑列表风险等级计算**：6 时间窗口矩阵判定逻辑
4. **Redis 模型风险存储**：key/field 格式正确，数据写入正确
5. **ES 审计日志查询**：range 查询 + terms 聚合正确
6. **upsert 写入逻辑**：updateSumByPrimaryKey 累加正确
7. **JSON 合并**：src_ip 和 model_risk JSON 字符串合并
8. **偏移量管理**：point_record 表更新正确
9. **每日资产统计**：漏洞/弱口令/攻击/访问风险统计正确
10. **数据清理**：reportDailyTasks 清理过期 report_main_stats 记录
