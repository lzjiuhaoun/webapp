# CLAUDE.md — report-engine 模块指令

## 模块角色

报表引擎：定时从 MySQL 风险表和统计表聚合数据，生成多维度报表统计结果，写入 MySQL 报表表供 web-app 消费。独立 JVM 进程部署。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| JDK | 1.8 | 运行环境 |
| Spring Boot | 2.5.15 | 基础框架 |
| MyBatis | 3.5.10 | MySQL ORM |
| Druid | 1.1.10 | 数据库连接池 |
| MySQL Connector | 8.0.29 | MySQL 驱动 |
| Elasticsearch | 7.17.20 | 审计日志查询 |
| Zookeeper | 3.8.4 | 服务注册 |
| Redis (Lettuce) | Spring Boot 管理 | 缓存 |
| Quartz | Spring Boot Quartz | 定时调度 |
| PageHelper | 1.4.5 | 分页插件 |
| common-module | 0.0.1-SNAPSHOT | Redis 工具、Zookeeper 客户端 |

## Source of Truth

- 源代码: ../src/main/java/com/ankki/reportengine/
- 配置: ../src/main/resources/application.properties
- 数据库映射: ../src/main/resources/mapper/
- POM: ../pom.xml
- 主类: com.ankki.reportengine.ReportEngineApplication

## 核心职责

1. **报表预统计**：每 10 分钟从 risk_result/statistic_result 表聚合数据，生成 8 种报表统计
2. **可疑列表预统计**：每小时生成可疑操作列表，计算风险等级
3. **每日模型统计**：每 5 个月 1 号凌晨 1:30 统计每日模型风险，写入 Redis
4. **每日资产统计**：每日凌晨 1:30 统计资产漏洞、弱口令、攻击风险等

## 运行配置

- JVM: `-Xmx1G -Xms1G`
- ZK 服务名: `reportengine`
- 定时任务: 4 个，频率从 10 分钟到 5 个月不等
- 打包: ProGuard 混淆 + AppAssembler
