# DEPENDENCIES.md — audit 审计模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| db-assets | `DbAssetsService` — 数据库信息 | >= v1.0.0 |
| strategy | `AuditStrategyService` — 策略同步 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| org.springframework.data:spring-data-elasticsearch | ES检索 |
| com.ankki:common-module | 公共工具类 |
